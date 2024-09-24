package org.isite.shop.service;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Error;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofDays;
import static org.isite.commons.cloud.utils.ResultUtils.isOk;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.DateUtils.PATTERN_DATE;
import static org.isite.commons.lang.utils.DateUtils.formatDate;
import static org.isite.operation.client.VipScoreAccessor.useVipScore;
import static org.isite.shop.support.constants.CacheKey.ORDER_NUMBER_DAY_COUNT_PREFIX;
import static org.isite.shop.support.enums.TradeStatus.NOTPAY;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TradeOrderService extends PoService<TradeOrderPo, Long> {

    private TradeOrderItemService itemService;
    private StringRedisTemplate redisTemplate;
    private CouponRecordService couponRecordService;

    @Autowired
    public TradeOrderService(TradeOrderMapper mapper) {
        super(mapper);
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

    /**
     * 基于时间戳生成订单号
     */
    public long generateOrderNumber() {
        String countKey = ORDER_NUMBER_DAY_COUNT_PREFIX + formatDate(new Date(), PATTERN_DATE);
        SimpleDateFormat orderNumberFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = orderNumberFormat.format(new Date(currentTimeMillis()));
        long count = redisTemplate.opsForValue().increment(countKey);
        if (ONE == count) {
            redisTemplate.expire(timestamp, ofDays(ONE));
        }
        return parseLong(timestamp + format("%04d", count));
    }
    /**
     * 只能删除未支付状态的订单
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteOrder(TradeOrderPo tradeOrderPo) {
        isTrue(NOTPAY.equals(tradeOrderPo.getTradeStatus()), new IllegalParameterError());
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
        orderItemPos.forEach(item -> item.setOrderId(tradeOrderPo.getId()));
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
        return tradeOrderPo.getId();
    }
}
