package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * 许愿活动属性
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class WishPrizeProperty extends ActivityProperty {
    /**
     * 满分
     */
    @Comment("满分")
    private Integer fullScore;
}
