package org.isite.misc.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.misc.data.vo.Region;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.isite.misc.data.constants.MiscUrls.PUBLIC_GET_PROVINCES;
import static org.isite.misc.data.constants.MiscUrls.PUBLIC_GET_REGIONS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "regionClient", value = SERVICE_ID)
public interface RegionClient {
    /**
     * 查询省份
     */
    @GetMapping(PUBLIC_GET_PROVINCES)
    Result<List<Region>> findProvince();
    /**
     * 根据父节点ID查询管辖范围
     */
    @GetMapping(PUBLIC_GET_REGIONS)
    Result<List<Region>> findRegions(@PathVariable("pid") Integer pid);
}
