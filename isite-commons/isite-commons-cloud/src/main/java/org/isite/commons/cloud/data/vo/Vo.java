package org.isite.commons.cloud.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
/**
 * @Description VO父类，用于Controller接口返回前端页面展示的数据
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Vo<I> implements Serializable {
    /**
     * 唯一标识
     */
    private I id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
