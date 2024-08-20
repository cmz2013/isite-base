package org.isite.misc.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.cache.RegionCache;
import org.isite.misc.data.vo.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.misc.data.constants.UrlConstants.PUBLIC_GET_PROVINCES;
import static org.isite.misc.data.constants.UrlConstants.PUBLIC_GET_REGIONS;

/**
 * @Description 地区Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class RegionController extends BaseController {

    private RegionCache regionCache;

    @Autowired
    public void setRegionCache(RegionCache regionCache) {
        this.regionCache = regionCache;
    }

    @GetMapping(PUBLIC_GET_REGIONS)
    public Result<List<Region>> findRegions(@PathVariable("pid") Integer pid) {
        return toResult(regionCache.getRegionByPid(pid));
    }

    @GetMapping(PUBLIC_GET_PROVINCES)
    public Result<List<Region>> findProvinces() {
        return toResult(regionCache.getRegionByPid(ZERO));
    }
}
