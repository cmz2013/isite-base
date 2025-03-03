package org.isite.data.converter;

import org.isite.commons.lang.Constants;
import org.isite.data.support.vo.ReportData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ReportConverter {

    private ReportConverter() {
    }

    /**
     * 统计接口调用次数
     */
    private static long toCallNumber(Map<Long, Integer> callDetail) {
        int number = 0;
        for (Integer value : callDetail.values()) {
            number += value;
        }
        return number;
    }

    /**
     * 创建ReportVo
     */
    public static ReportData toReportData(
            long executorNumber, long apiNumber, Map<Long, Integer> callLatest, Map<Long, Integer> callFailure) {
        ReportData reportData = new ReportData();
        reportData.setExecutorNumber(executorNumber);
        reportData.setApiNumber(apiNumber);
        reportData.setCallLatestNumber(toCallNumber(callLatest));
        reportData.setCallFailureNumber(toCallNumber(callFailure));

        List<Integer> successDetail = new ArrayList<>(callLatest.size());
        List<Integer> failureDetail = new ArrayList<>(callLatest.size());
        List<String> callTimes = new ArrayList<>(callLatest.size());
        callLatest.keySet().stream().sorted().forEach(key -> {
            String time = key.toString().substring(Constants.EIGHT);
            callTimes.add(time.substring(Constants.ZERO, Constants.TWO) + Constants.COLON + time.substring(Constants.TWO));
            int failureNumber = callFailure.getOrDefault(key, Constants.ZERO);
            successDetail.add(callLatest.get(key) - failureNumber);
            failureDetail.add(failureNumber);
        });
        reportData.setCallTimes(callTimes);
        reportData.setCallSuccessDetails(successDetail);
        reportData.setCallFailureDetails(failureDetail);
        return reportData;
    }
}
