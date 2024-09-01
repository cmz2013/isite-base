package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * @Description 许愿活动属性
 * @Author <font color='blue'>zhangcm</font>
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
