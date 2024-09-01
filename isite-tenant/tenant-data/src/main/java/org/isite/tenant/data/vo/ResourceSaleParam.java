package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description sku供应商参数：资源销售参数.
 * 1）资源不能单独购买，而是以套餐形式购买：即个人版、企业版、旗舰版等。
 * 2）套餐升级不会重复添加资源。套餐降级不会删除用户历史数据，但是，可能会导致部分历史数据无权限查看。
 * 3）用户在选择套餐续期（租户续期）时，系统自动生成该租户待支付订单信息。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ResourceSaleParam {
    /**
     * 租户ID不为空时，表示续期操作，否则表示购买操作。
     */
    private Integer tenantId;
    /**
     * 有效期天数
     */
    private Integer expireDays;
    /**
     * 套餐资源ID
     */
    private List<Integer> resourceIds;
}
