package org.isite.shop.converter;

import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.support.dto.PayNoticeDto;
import org.isite.shop.support.dto.TradeOrderSkuDto;
import org.isite.shop.support.dto.TradeOrderSupplierDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.isite.shop.support.enums.TradeStatus.SUCCESS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TradeOrderConverter {

    private TradeOrderConverter() {
    }

    public static TradeOrderPo toTradeOrderSelectivePo(Long id, PayNoticeDto payNoticeDto) {
        TradeOrderPo tradeOrderPo = new TradeOrderPo();
        tradeOrderPo.setId(id);
        tradeOrderPo.setTradeStatus(SUCCESS);
        tradeOrderPo.setPaymentType(payNoticeDto.getPaymentType());
        tradeOrderPo.setPaymentNumber(payNoticeDto.getPaymentNumber());
        tradeOrderPo.setPayTime(payNoticeDto.getPayTime());
        return tradeOrderPo;
    }

    public static TradeOrderSupplierDto toTradeOrderSupplierDto(
            TradeOrderPo tradeOrderPo, List<TradeOrderItemPo> orderItemPos) {
        TradeOrderSupplierDto tradeOrderSupplierDto = new TradeOrderSupplierDto();
        tradeOrderSupplierDto.setUserId(tradeOrderPo.getUserId());
        tradeOrderSupplierDto.setSkus(orderItemPos.stream().map(TradeOrderConverter::toTradeOrderSkuDto).collect(toList()));
        return tradeOrderSupplierDto;
    }

    private static TradeOrderSkuDto toTradeOrderSkuDto(TradeOrderItemPo orderItemPo) {
        TradeOrderSkuDto tradeOrderSkuDto = new TradeOrderSkuDto();
        tradeOrderSkuDto.setSpuName(orderItemPo.getSpuName());
        tradeOrderSkuDto.setSupplierParam(orderItemPo.getSupplierParam());
        tradeOrderSkuDto.setSkuCount(orderItemPo.getSkuCount());
        return tradeOrderSkuDto;
    }
}
