package org.isite.misc.cache;

import com.alicp.jetcache.anno.Cached;
import org.isite.misc.service.RegionService;
import org.isite.misc.data.vo.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.alicp.jetcache.anno.CacheType.BOTH;
import static java.util.stream.Collectors.toList;
import static org.isite.commons.lang.data.Constants.DAY_SECONDS;
import static org.isite.commons.lang.data.Constants.MINUTE_SECONDS;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;
import static org.isite.misc.converter.RegionConverter.toRegion;
import static org.isite.misc.data.constants.CacheKey.REGION_LIST_PREFIX;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Component
public class RegionCache {

    private RegionService regionService;

    @Autowired
    public void setRegionService(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * @Description 根据父节点ID查询地区，包含状态为可用的和禁用的
     * 使用两级缓存（远程+本地），避免高频率访问redis
     * 修改地区的时候，需要根据pid删除缓存
     */
    @Cached(name = REGION_LIST_PREFIX, key = "#pid", cacheType = BOTH, expire = DAY_SECONDS, localExpire = MINUTE_SECONDS)
    public List<Region> getRegionByPid(int pid) {
        String fullName = regionService.getFullName(pid);
        return regionService.findByPid(pid, ENABLED).stream().map(regionPo ->
                toRegion(regionPo, fullName + regionPo.getName())).collect(toList());
    }
}
