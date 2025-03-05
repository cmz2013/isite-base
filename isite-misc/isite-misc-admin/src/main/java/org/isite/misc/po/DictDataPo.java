package org.isite.misc.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "dict_data")
public class DictDataPo extends Po<Integer> {
    /**
     * 字典类型
     */
    private String type;
    /**
     * 字典标签
     */
    private String label;
    /**
     * 字典值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
}
