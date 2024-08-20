package org.isite.data.converter;

import org.isite.data.support.vo.ReportData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.isite.commons.lang.data.Constants.COLON;
import static org.isite.commons.lang.data.Constants.TWO;
import static org.isite.commons.lang.data.Constants.ZERO;

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
            String time = key.toString().substring(8);
            callTimes.add(time.substring(ZERO, TWO) + COLON + time.substring(TWO));
            int failureNumber = callFailure.getOrDefault(key, ZERO);
            successDetail.add(callLatest.get(key) - failureNumber);
            failureDetail.add(failureNumber);
        });
        reportData.setCallTimes(callTimes);
        reportData.setCallSuccessDetails(successDetail);
        reportData.setCallFailureDetails(failureDetail);
        return reportData;
    }
}
