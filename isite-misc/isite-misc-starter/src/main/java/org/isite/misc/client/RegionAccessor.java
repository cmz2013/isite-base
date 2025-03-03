package org.isite.misc.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.misc.data.constants.MiscConstants;
import org.isite.misc.data.vo.Region;

import java.util.List;
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
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        RegionClient regionClient = feignClientFactory.getFeignClient(RegionClient.class, MiscConstants.SERVICE_ID);
        return ResultUtils.getData(regionClient.findProvince());
    }

    /**
     * 根据父节点ID查询管辖范围
     */
    public static List<Region> findRegions(Integer pid) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        RegionClient regionClient = feignClientFactory.getFeignClient(RegionClient.class, MiscConstants.SERVICE_ID);
        return ResultUtils.getData(regionClient.findRegions(pid));
    }

}
