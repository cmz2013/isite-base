package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;
import org.isite.shop.support.enums.SpuSupplier;

/**
 * @Description 产品信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Spu extends Vo<Integer> {
    /**
     * 产品名称
     */
    private String spuName;
    /**
     * 供应商
     */
    private SpuSupplier supplier;
    /**
     * 供应商自定义参数
     */
    private String supplierParam;
}