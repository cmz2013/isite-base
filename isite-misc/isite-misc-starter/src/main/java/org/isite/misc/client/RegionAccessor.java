package org.isite.misc.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.misc.data.vo.Region;

import java.util.List;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.ResultUtils.getData;
import static org.isite.misc.data.constants.MiscConstants.SERVICE_ID;

/**
 * @Description RegionClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class RegionAccessor {

    private RegionAccessor() {
    }

    /**
     * 查询省份
     */
    public static List<Region> findProvince() {
        RegionClient regionClient = getBean(FeignClientFactory.class).getFeignClient(RegionClient.class, SERVICE_ID);
        return getData(regionClient.findProvince());
    }

    /**
     * 根据父节点ID查询管辖范围
     */
    public static List<Region> findRegions(Integer pid) {
        RegionClient regionClient = getBean(FeignClientFactory.class).getFeignClient(RegionClient.class, SERVICE_ID);
        return getData(regionClient.findRegions(pid));
    }

}
