package org.isite.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.data.ListRequest;
import org.isite.commons.cloud.data.ListResult;
import org.isite.commons.cloud.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.shop.po.CouponRecordPo;
import org.isite.shop.po.SkuPo;
import org.isite.shop.po.SpuPo;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.service.CouponRecordService;
import org.isite.shop.service.SkuService;
import org.isite.shop.service.SpuService;
import org.isite.shop.service.TradeOrderService;
import org.isite.shop.support.dto.TradeOrderGetDto;
import org.isite.shop.support.dto.TradeOrderItemPostDto;
import org.isite.shop.support.vo.TradeOrderBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.cloud.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toListQuery;
import static org.isite.commons.cloud.data.Converter.toMap;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getClientIdentifier;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderItemSelectivePos;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderPo;
import static org.isite.shop.converter.TradeOrderConverter.toTradeOrderSelectivePo;
import static org.isite.shop.support.constants.UrlConstants.URL_SHOP;
import static org.isite.user.client.UserAccessor.getUserDetails;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@RestController
public class TradeOrderController extends BaseController {

    private SpuService spuService;
    private SkuService skuService;
    private CouponRecordService couponRecordService;
    private TradeOrderService tradeOrderService;

    @Autowired
    public void setSpuService(SpuService spuService) {
        this.spuService = spuService;
    }

    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    @Autowired
    public void setCouponRecordService(CouponRecordService couponRecordService) {
        this.couponRecordService = couponRecordService;
    }

    @Autowired
    public void setTradeOrderService(TradeOrderService tradeOrderService) {
        this.tradeOrderService = tradeOrderService;
    }

    /**
     * 分页查询当前用户的订单列表，不查询总条数。建议倒序查询，最新订单在前面。
     */
    @GetMapping(URL_MY + URL_SHOP + "/order")
    public ListResult<TradeOrderBasic> findList(@Validated ListRequest<TradeOrderGetDto> request) {
        List<TradeOrderPo> tradeOrderPos = tradeOrderService.findList(toListQuery(request,
                () -> toTradeOrderSelectivePo(getUserId(), request.getQuery())));
        return toListResult(request, convert(tradeOrderPos, TradeOrderBasic::new));
    }

    /**
     * @Description 新增订单
     * @return 订单ID
     */
    @PostMapping(URL_MY + URL_SHOP + "/order")
    public Result<Long> addOrder(@Validated @NotEmpty List<TradeOrderItemPostDto> orderItemPostDtos) {
        List<SkuPo> skuPos = skuService.findIn(SkuPo::getId, convert(orderItemPostDtos, TradeOrderItemPostDto::getSkuId));
        List<SpuPo> spuPos = spuService.findIn(SpuPo::getId, convert(skuPos, SkuPo::getSpuId));
        List<Integer> couponRecordIds = convert(orderItemPostDtos, TradeOrderItemPostDto::getCouponRecordId);
        Map<Integer, CouponRecordPo> couponRecordPos = isEmpty(couponRecordIds) ? null :
                toMap(couponRecordService.findIn(CouponRecordPo::getId, couponRecordIds), CouponRecordPo::getId);
        List<TradeOrderItemPo> orderItemPos = toTradeOrderItemSelectivePos(getUserDetails(getUserId()),
                orderItemPostDtos, toMap(spuPos, SpuPo::getId), toMap(skuPos, SkuPo::getId), couponRecordPos);
        TradeOrderPo tradeOrderPo = toTradeOrderPo(getUserId(), getClientIdentifier(), tradeOrderService.generateOrderNumber(),
                orderItemPos.stream().mapToInt(TradeOrderItemPo::getPayPrice).sum());
        return toResult(tradeOrderService.addOrder(tradeOrderPo, orderItemPos, couponRecordIds));
    }

    /**
     * 只能删除未支付状态的订单
     */
    @DeleteMapping(URL_MY + URL_SHOP + "/order/{id}")
    public Result<Integer> deleteOrder(@PathVariable("id") Long id) {
        TradeOrderPo tradeOrderPo = tradeOrderService.get(id);
        isTrue(tradeOrderPo.getUserId().equals(getUserId()), new OverstepAccessError());
        return toResult(tradeOrderService.deleteOrder(tradeOrderPo));
    }
}
