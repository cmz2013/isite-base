package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.operation.data.enums.ActivityTheme;

import java.util.Date;
import java.util.List;

/**
 * @Description 运营活动是一种比较通用的活动，用户通过完成运营任务获取运营奖品。
 * 1) 活动一旦结束如果修改时间重新开始，有些运营任务会因为约束条件不能重复执行。
 * 2) 主子活动 注意事项：
 * > 活动信息不返回子活动，每个活动信息独立缓存
 * > 主子活动支持两级
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Activity extends Vo<Integer> {
    /**
     * 主活动ID（主子活动，支持两级）
     */
    private Integer pid;
    /**
     * 活动标题
     */
    private String title;
    /**
     * 活动主题
     */
    private ActivityTheme theme;
    /**
     * 活动属性
     */
    private ActivityProperty property;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 奖品信息
     */
    private List<Prize> prizes;
    /**
     * 任务列表
     */
    private List<Task> tasks;
    /**
     * 上架状态
     */
    private SwitchStatus status;
    /**
     * 备注
     */
    private String remark;
}
