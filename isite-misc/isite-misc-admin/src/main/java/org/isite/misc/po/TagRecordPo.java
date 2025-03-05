package org.isite.misc.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.misc.data.enums.ObjectType;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "tag_record")
public class TagRecordPo extends Po<Integer> {

    private Integer tagId;

    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ObjectType objectType;
    /**
     * 数据对象值（ID等）
     */
    private String objectValue;
}
