package org.isite.data.client;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.data.support.vo.DataApi;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ParameterGenerator {
    /**
     * 生成接口参数
     */
    public Map<String, Object> generate(DataApi api, Object... data) {
        Map<String, Object> results = new HashMap<>();
        String[] argNames = StringUtils.split(api.getArgs(), ",");
        if (ArrayUtils.isNotEmpty(argNames)) {
            for (int i = 0; i < argNames.length; i++) {
                if (data.length > i) {
                    results.put(argNames[i], data[i]);
                }
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                results.put("arg" + i, data[i]);
            }
        }
        return results;
    }
}
