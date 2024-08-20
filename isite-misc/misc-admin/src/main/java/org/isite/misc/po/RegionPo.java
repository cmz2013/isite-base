package org.isite.misc.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.mybatis.data.TreePo;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * 地区(Region)表实体类
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "region")
public class RegionPo extends TreePo<Integer> {
    /**
     * 地区名称
     */
    private String name;
    /**
     * 地区编码
     *
     * 引用地区的表存储编码，sql中使用 region_code like '01%' 查询省份管辖范围的数据（使用region_code字段上的索引）
     * 右模糊查询管辖范围数据简单高效，新增层级易扩展
     *
     * 存储ID，查询管辖范围数据复杂低效（使用in或者join查询），新增层级不宜扩展（数据表要新增字段）
     */
    private String code;
    /**
     * 状态
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private SwitchStatus status;
}

