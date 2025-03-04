package org.isite.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.ListQueryConverter;
import org.isite.commons.cloud.converter.MapConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.dto.ListRequest;
import org.isite.commons.cloud.data.vo.ListResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.security.data.enums.ClientIdentifier;
import org.isite.shop.converter.TradeOrderConverter;
import org.isite.shop.po.CouponRecordPo;
import org.isite.shop.po.SkuPo;
import org.isite.shop.po.SpuPo;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.isite.shop.service.CouponRecordService;
import org.isite.shop.service.SkuService;
import org.isite.shop.service.SpuService;
import org.isite.shop.service.TradeOrderService;
import org.isite.shop.support.constants.ShopUrls;
import org.isite.shop.support.dto.TradeOrderGetDto;
import org.isite.shop.support.dto.TradeOrderItemPostDto;
import org.isite.shop.support.vo.TradeOrderBasic;
import org.isite.user.client.UserAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

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
    @GetMapping(UrlConstants.URL_MY + ShopUrls.URL_SHOP + "/order")
    public ListResult<TradeOrderBasic> findList(@Validated ListRequest<TradeOrderGetDto> request) {
        List<TradeOrderPo> tradeOrderPos = tradeOrderService.findList(ListQueryConverter.toListQuery(request,
                () -> TradeOrderConverter.toTradeOrderSelectivePo(TransmittableHeaders.getUserId(), request.getQuery())));
        return toListResult(request, DataConverter.convert(tradeOrderPos, TradeOrderBasic::new));
    }

    /**
     * @Description 新增订单
     * @return 订单ID
     */
    @Validated
    @PostMapping(UrlConstants.URL_MY + ShopUrls.URL_SHOP + "/order")
    public Result<Long> addOrder(
            @RequestParam(value = "client", required = false) ClientIdentifier client,
            @RequestBody @NotEmpty List<TradeOrderItemPostDto> itemPostDtos) {
        List<SkuPo> skuPos = skuService.findIn(SkuPo::getId,
                DataConverter.convert(itemPostDtos, TradeOrderItemPostDto::getSkuId));
        List<SpuPo> spuPos = spuService.findIn(SpuPo::getId,
                DataConverter.convert(skuPos, SkuPo::getSpuId));
        List<Integer> couponRecordIds = DataConverter.convert(itemPostDtos, TradeOrderItemPostDto::getCouponRecordId);
        Map<Integer, CouponRecordPo> couponRecordPos = CollectionUtils.isEmpty(couponRecordIds) ? null :
                MapConverter.toMap(CouponRecordPo::getId, couponRecordService.findIn(CouponRecordPo::getId, couponRecordIds));
        long userId = TransmittableHeaders.getUserId();
        List<TradeOrderItemPo> orderItemPos = TradeOrderConverter.toTradeOrderItemSelectivePos(
                UserAccessor.getUserDetails(userId), itemPostDtos, MapConverter.toMap(SpuPo::getId, spuPos),
                MapConverter.toMap(SkuPo::getId, skuPos), couponRecordPos);
        TradeOrderPo tradeOrderPo = TradeOrderConverter.toTradeOrderPo(userId, tradeOrderService.generateOrderNo(),
                orderItemPos.stream().mapToInt(TradeOrderItemPo::getPayPrice).sum(), client);
        return toResult(tradeOrderService.addOrder(tradeOrderPo, orderItemPos, couponRecordIds));
    }

    /**
     * 只能删除未支付状态的订单
     */
    @DeleteMapping(UrlConstants.URL_MY + ShopUrls.URL_SHOP + "/order/{id}")
    public Result<Integer> deleteOrder(@PathVariable("id") Long id) {
        TradeOrderPo tradeOrderPo = tradeOrderService.get(id);
        Assert.isTrue(tradeOrderPo.getUserId().equals(TransmittableHeaders.getUserId()), new OverstepAccessError());
        return toResult(tradeOrderService.deleteOrder(tradeOrderPo));
    }
}
