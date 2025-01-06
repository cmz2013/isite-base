package org.isite.misc.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Description 通过字典类型下钻，查看和添加字典数据
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "dict_type")
public class DictTypePo extends Po<Integer> {
    /**
     * 字典名称（页面展示用）
     */
    private String name;
    /**
     * 字典类型值（唯一标识，程序使用）
     */
    private String value;
    /**
     * 备注
     */
    private String remark;
}
