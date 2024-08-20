package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.misc.data.enums.ObjectType;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TagRecord extends Vo<Integer> {

    private Integer tagId;
    private ObjectType objectType;
    /**
     * 数据对象值（ID等）
     */
    private String objectValue;
}
