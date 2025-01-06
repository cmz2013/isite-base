package org.isite.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Error;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofDays;
import static org.isite.commons.cloud.converter.MapConverter.groupBy;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.cloud.utils.ResultUtils.isOk;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.DateUtils.PATTERN_DATE;
import static org.isite.commons.lang.utils.DateUtils.formatDate;
import static org.isite.operation.client.VipScoreAccessor.useVipScore;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderSupplierDto;
import static org.isite.shop.support.constants.CacheKey.ORDER_NUMBER_DAY_COUNT_PREFIX;
import static org.isite.shop.support.constants.ShopConstants.EXCHANGE_TRADE_ORDER_SUCCESS;
import static org.isite.shop.support.enums.TradeStatus.CLOSED;
import static org.isite.shop.support.enums.TradeStatus.SUCCESS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Service
public class TradeOrderService extends PoService<TradeOrderPo, Long> {

    private SkuService skuService;
    private TradeOrderItemService itemService;
    private StringRedisTemplate redisTemplate;
    private CouponRecordService couponRecordService;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public TradeOrderService(TradeOrderMapper mapper) {
        super(mapper);
    }

    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    @Autowired
    public void setItemService(TradeOrderItemService itemService) {
        this.itemService = itemService;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setCouponRecordService(CouponRecordService couponRecordService) {
        this.couponRecordService = couponRecordService;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 基于时间戳生成订单号
     */
    public long generateOrderNo() {
        String date = formatDate(new Date(currentTimeMillis()), PATTERN_DATE);
        String countKey = ORDER_NUMBER_DAY_COUNT_PREFIX + date;
        Long count = redisTemplate.opsForValue().increment(countKey);
        if (null != count) {
            if (count.intValue() == ONE) {
                redisTemplate.expire(countKey, ofDays(ONE));
            }
            //确保数字至少有 3 位，不足的部分用 0 填充。如果你传入的数字大于 3 位，会根据数字的实际位数显示
            return parseLong(date + format("%03d", count));
        }
        throw new RuntimeException("Failed to generate an order number");
    }
    /**
     * 只能删除状态为交易关闭的订单
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteOrder(TradeOrderPo tradeOrderPo) {
        isTrue(CLOSED.equals(tradeOrderPo.getStatus()), new IllegalParameterError());
        itemService.delete(TradeOrderItemPo::getOrderId, tradeOrderPo.getId());
        return this.delete(tradeOrderPo.getId());
    }

    /**
     * 添加订单
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addOrder(TradeOrderPo tradeOrderPo, List<TradeOrderItemPo> orderItemPos,
                         List<Integer> couponRecordIds) {
        //保存订单信息
        this.insert(tradeOrderPo);
        orderItemPos.forEach(orderItemPo -> {
            orderItemPo.setOrderId(tradeOrderPo.getId());
            //更新商品已售出数量
            isTrue(skuService.updateSoldNum(orderItemPo.getSkuId(), orderItemPo.getSkuNum()) > ZERO,
                    getMessage("sku.stock.notEnough", "The item you ordered is out of stock"));
        });
        //保存订单条目明细
        itemService.insert(orderItemPos);
        //更新用户的优惠券状态
        couponRecordService.updateCouponUsed(couponRecordIds);
        //会员积分抵扣
        int payScore = orderItemPos.stream().mapToInt(TradeOrderItemPo::getPayScore).sum();
        if (payScore > ZERO) {
            Result<?> result = useVipScore(payScore);
            isTrue(isOk(result), new Error(result.getCode(), result.getMessage()));
        }
        if (SUCCESS.equals(tradeOrderPo.getStatus())) {
            sendOrderSuccessMessage(tradeOrderPo, orderItemPos);
        }
        return tradeOrderPo.getId();
    }

    public void sendOrderSuccessMessage(TradeOrderPo tradeOrderPo, List<TradeOrderItemPo> orderItemPos) {
        try {
            groupBy(TradeOrderItemPo::getSupplier, orderItemPos).forEach((supplier, pos) -> rabbitTemplate.convertAndSend(
                    EXCHANGE_TRADE_ORDER_SUCCESS, supplier.getCode(), toTradeOrderSupplierDto(tradeOrderPo, pos)));
        } catch (Exception e) {
            log.error("send order success message error: " + tradeOrderPo.getOrderNo(), e);
        }
    }
}
