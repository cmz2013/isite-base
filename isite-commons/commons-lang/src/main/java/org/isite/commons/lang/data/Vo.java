package org.isite.commons.lang.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description VO父类，用于Controller接口返回前端页面展示的数据
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Vo<I> {
    /**
     * 唯一标识
     */
    private I id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
