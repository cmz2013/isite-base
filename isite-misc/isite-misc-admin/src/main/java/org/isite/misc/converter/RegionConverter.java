package org.isite.misc.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.misc.data.vo.Region;
import org.isite.misc.po.RegionPo;

import java.util.ArrayList;
import java.util.List;
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
        Region region = DataConverter.convert(regionPo, Region::new);
        region.setFullName(fullName);
        return region;
    }

    /**
     * 解析code，按顺序返回父节点code
     */
    public static List<String> toPcodes(String code) {
        int index = (code.length() - Constants.TWO) / Constants.TWO;
        List<String> codes = new ArrayList<>(index);
        for (int i = Constants.ONE; i <= index; i++) {
            codes.add(code.substring(Constants.ZERO, i * Constants.TWO));
        }
        return codes;
    }
}
