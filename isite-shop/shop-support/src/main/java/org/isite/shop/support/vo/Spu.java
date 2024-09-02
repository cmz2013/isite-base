package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;

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
    private String supplier;

}