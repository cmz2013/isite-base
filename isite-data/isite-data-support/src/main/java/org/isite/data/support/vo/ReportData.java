package org.isite.data.support.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description 运行报表数据
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ReportData {
    /**
     * 启用的接口数量
     */
    private long apiNumber;
    /**
     * 执行器数量
     */
    private long executorNumber;
    /**
     * 接口调用时间（HH:mm）
     */
    private List<String> callTimes;
    /**
     * 接口最近1小时调用成功次数（每分钟的调用次数）
     */
    private List<Integer> callSuccessDetails;
    /**
     * 接口最近1小时调用失败次数(每分钟的失败日志个数)
     */
    private List<Integer> callFailureDetails;
    /**
     * 最近1小时接口调用的次数
     */
    private long callLatestNumber;
    /**
     * 最近1小时接口调用失败的次数
     */
    private long callFailureNumber;
}
