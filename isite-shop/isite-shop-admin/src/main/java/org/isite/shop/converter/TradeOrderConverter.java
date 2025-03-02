package org.isite.shop.converter;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.security.data.enums.ClientIdentifier;
import org.isite.shop.po.CouponRecordPo;
import org.isite.shop.po.SkuPo;
import org.isite.shop.po.SpuPo;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.support.dto.PayNoticeDto;
import org.isite.shop.support.dto.TradeOrderGetDto;
import org.isite.shop.support.dto.TradeOrderItemPostDto;
import org.isite.shop.support.dto.TradeOrderSkuDto;
import org.isite.shop.support.dto.TradeOrderSupplierDto;
import org.isite.shop.support.enums.PaymentType;
import org.isite.shop.support.enums.TradeStatus;
import org.isite.user.data.vo.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TradeOrderConverter {

    private TradeOrderConverter() {
    }

    public static TradeOrderPo toTradeOrderPo(
            Long userId, Long orderNo, int payPrice, ClientIdentifier client) {
        TradeOrderPo tradeOrderPo = new TradeOrderPo();
        tradeOrderPo.setUserId(userId);
        tradeOrderPo.setOrderTime(LocalDateTime.now());
        tradeOrderPo.setOrderNo(orderNo);
        tradeOrderPo.setClientIdentifier(client);
        tradeOrderPo.setPayPrice(payPrice);
        //订单支付金额为0时，订单状态为支付完成, 否则订单状态为待支付
        tradeOrderPo.setStatus(Constants.ZERO == payPrice ? TradeStatus.SUCCESS : TradeStatus.PENDING);
        tradeOrderPo.setServiceCharge(Constants.ZERO);
        tradeOrderPo.setPaymentNo(0L);
        tradeOrderPo.setPaymentType(PaymentType.ALIPAY);
        return tradeOrderPo;
    }

    public static TradeOrderPo toTradeOrderSelectivePo(Long userId, TradeOrderGetDto tradeOrderGetDto) {
        TradeOrderPo tradeOrderPo = DataConverter.convert(tradeOrderGetDto, TradeOrderPo::new);
        tradeOrderPo.setUserId(userId);
        return tradeOrderPo;
    }

    public static TradeOrderPo toTradeOrderSelectivePo(Long orderId, PayNoticeDto payNoticeDto) {
        TradeOrderPo tradeOrderPo = new TradeOrderPo();
        tradeOrderPo.setId(orderId);
        tradeOrderPo.setStatus(TradeStatus.SUCCESS);
        tradeOrderPo.setServiceCharge(payNoticeDto.getServiceCharge());
        tradeOrderPo.setPaymentType(payNoticeDto.getPaymentType());
        tradeOrderPo.setPaymentNo(payNoticeDto.getPaymentNo());
        tradeOrderPo.setPayTime(payNoticeDto.getPayTime());
        return tradeOrderPo;
    }

    public static TradeOrderSupplierDto toTradeOrderSupplierDto(
            TradeOrderPo tradeOrderPo, List<TradeOrderItemPo> orderItemPos) {
        TradeOrderSupplierDto tradeOrderSupplierDto = new TradeOrderSupplierDto();
        tradeOrderSupplierDto.setUserId(tradeOrderPo.getUserId());
        tradeOrderSupplierDto.setSkuDtos(
                orderItemPos.stream().map(TradeOrderConverter::toTradeOrderSkuDto).collect(Collectors.toList()));
        return tradeOrderSupplierDto;
    }

    private static TradeOrderSkuDto toTradeOrderSkuDto(TradeOrderItemPo orderItemPo) {
        TradeOrderSkuDto tradeOrderSkuDto = new TradeOrderSkuDto();
        tradeOrderSkuDto.setSpuName(orderItemPo.getSpuName());
        tradeOrderSkuDto.setSupplierParam(orderItemPo.getSupplierParam());
        tradeOrderSkuDto.setSkuNum(orderItemPo.getSkuNum());
        return tradeOrderSkuDto;
    }

    /**
     * 按支付金额比例分摊服务费
     */
    public static List<TradeOrderItemPo> toTradeOrderItemSelectivePos(
            Integer payPrice, List<TradeOrderItemPo> orderItemPos, Integer serviceCharge) {
        int lastIndex = orderItemPos.size() - Constants.ONE;
        List<TradeOrderItemPo> results = new ArrayList<>(orderItemPos.size());
        for (int i = Constants.ZERO; i < lastIndex; i++) {
            results.add(toTradeOrderItemSelectivePo(orderItemPos.get(i).getId(),
                    orderItemPos.get(i).getPayPrice() * serviceCharge / payPrice));
        }
        //mapToInt 可以帮助简化从对象转换为数值基本类型的操作，使得在处理数字计算时更加直观和高效。
        results.add(toTradeOrderItemSelectivePo(orderItemPos.get(lastIndex).getId(),
                serviceCharge - results.stream().mapToInt(TradeOrderItemPo::getServiceCharge).sum()));
        return results;
    }

    private static TradeOrderItemPo toTradeOrderItemSelectivePo(Long id, int serviceCharge) {
        TradeOrderItemPo tradeOrderItemPo = new TradeOrderItemPo();
        tradeOrderItemPo.setId(id);
        tradeOrderItemPo.setServiceCharge(serviceCharge);
        return tradeOrderItemPo;
    }

    public static List<TradeOrderItemPo> toTradeOrderItemSelectivePos(
            UserDetails userDetails, List<TradeOrderItemPostDto> orderItemPostDtos, Map<Integer, SpuPo> spuPos,
            Map<Integer, SkuPo> skuPos, Map<Integer, CouponRecordPo> couponRecordPos) {
        List<TradeOrderItemPo> results = new ArrayList<>(orderItemPostDtos.size());
        for (TradeOrderItemPostDto orderItemPostDto : orderItemPostDtos) {
            results.add(toTradeOrderItemSelectivePo(userDetails, orderItemPostDto, spuPos, skuPos, couponRecordPos));
        }
        return results;
    }

    private static TradeOrderItemPo toTradeOrderItemSelectivePo(
            UserDetails userDetails, TradeOrderItemPostDto orderItemPostDto, Map<Integer, SpuPo> spuPos,
            Map<Integer, SkuPo> skuPos, Map<Integer, CouponRecordPo> couponRecordPos) {
        TradeOrderItemPo tradeOrderItemPo = new TradeOrderItemPo();
        tradeOrderItemPo.setSkuId(orderItemPostDto.getSkuId());
        tradeOrderItemPo.setSkuNum(orderItemPostDto.getSkuNum());
        SkuPo skuPo = skuPos.get(orderItemPostDto.getSkuId());
        tradeOrderItemPo.setSkuId(skuPo.getId());
        tradeOrderItemPo.setMarketPrice(skuPo.getMarketPrice());
        tradeOrderItemPo.setCostPrice(skuPo.getCostPrice());
        tradeOrderItemPo.setSalePrice(userDetails.isVip() ? skuPo.getVipPrice() : skuPo.getSalePrice());
        SpuPo spuPo = spuPos.get(skuPo.getSpuId());
        tradeOrderItemPo.setSpuId(spuPo.getId());
        tradeOrderItemPo.setSpuName(spuPo.getSpuName());
        tradeOrderItemPo.setSupplier(spuPo.getSupplier());
        tradeOrderItemPo.setSupplierParam(spuPo.getSupplierParam());
        tradeOrderItemPo.setServiceCharge(Constants.ZERO);

        int payPrice = tradeOrderItemPo.getSalePrice() * tradeOrderItemPo.getSkuNum();
        if (null != orderItemPostDto.getCouponRecordId()) {
            CouponRecordPo couponRecordPo = couponRecordPos.get(orderItemPostDto.getCouponRecordId());
            Assert.isTrue(couponRecordPo.getUserId().equals(userDetails.getId()), new OverstepAccessError());
            Assert.isFalse(couponRecordPo.getUsed(), "coupon has been used");
            Assert.isTrue(couponRecordPo.getExpireTime().isAfter(LocalDateTime.now()), "coupon has expired");
            String skuIds = couponRecordPo.getSkuIds();
            if (StringUtils.isNotBlank(skuIds)) {
                Assert.isTrue(Arrays.asList(skuIds.split(Constants.COMMA)).contains(tradeOrderItemPo.getSkuId().toString()),
                        "coupon is not applicable to this item");
            }
            tradeOrderItemPo.setDiscountPrice(couponRecordPo.getDiscountPrice());
            payPrice = payPrice - tradeOrderItemPo.getDiscountPrice();
            Assert.isTrue(payPrice >= Constants.ZERO, "coupon has been overpaid");
        } else {
            tradeOrderItemPo.setDiscountPrice(Constants.ZERO);
        }
        //计算订单条目实际支付金额，优惠券优先级大于会员积分抵扣
        if (null != tradeOrderItemPo.getPayScore()) {
            tradeOrderItemPo.setPayScore(orderItemPostDto.getPayScore());
            Assert.isTrue(payPrice >= tradeOrderItemPo.getPayScore(), "score has been overpaid");
            payPrice = payPrice - tradeOrderItemPo.getPayScore();
        } else {
            tradeOrderItemPo.setPayScore(Constants.ZERO);
        }
        tradeOrderItemPo.setPayPrice(payPrice);
        return tradeOrderItemPo;
    }
}
