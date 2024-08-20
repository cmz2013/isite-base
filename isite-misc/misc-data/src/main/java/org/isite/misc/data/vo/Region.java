package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Tree;
import org.isite.commons.lang.enums.SwitchStatus;

/**
 * @Description 地区(Region)
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Region extends Tree<Region, Integer> {
    /**
     * 地区名称
     */
    private String name;
    /**
     * 地区完整名称
     */
    private String fullName;
    /**
     * 地区编码
     * [乡及以下地区编码规则]
     * 1）编码方式：根据层级顺序（从小到大），依次编码（00~99）
     * 2）编码长度：省：2位，市：4位，县：6位，乡：8位，村：10位
     * 例如，北京海淀区编码为： 0101
     * 3）计数编码算法：根据父节点ID，count子节点个数+1，不足两位高位补0
     *
     * [省市县地区编码规则]
     * 1）沿用国家统一编码，截掉父节点低位补的0（不统一6位）
     * 2）注意：6位编码的父节点编码个别是2位，并非都是4位
     */
    private String code;
    /**
     * 状态
     */
    private SwitchStatus status;
}

