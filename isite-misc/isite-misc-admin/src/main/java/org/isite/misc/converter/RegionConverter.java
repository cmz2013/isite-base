package org.isite.misc.converter;

import org.isite.misc.data.vo.Region;
import org.isite.misc.po.RegionPo;

import java.util.ArrayList;
import java.util.List;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.TWO;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Description 地区数据转换
 * @Author <font color='blue'>zhangcm</font>
 */
public class RegionConverter {

    private RegionConverter() {
    }

    /**
     * 将po转换为dto
     */
    public static Region toRegion(RegionPo regionPo, String fullName) {
        if (null == regionPo) {
            return null;
        }
        Region region = convert(regionPo, Region::new);
        region.setFullName(fullName);
        return region;
    }

    /**
     * 解析code，按顺序返回父节点code
     */
    public static List<String> toPcodes(String code) {
        int index = (code.length() - TWO) / TWO;
        List<String> codes = new ArrayList<>(index);
        for (int i = ONE; i <= index; i++) {
            codes.add(code.substring(ZERO, i * TWO));
        }
        return codes;
    }
}
