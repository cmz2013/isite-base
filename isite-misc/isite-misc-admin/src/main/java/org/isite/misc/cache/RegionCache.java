package org.isite.misc.cache;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.misc.converter.RegionConverter;
import org.isite.misc.data.constants.CacheKeys;
import org.isite.misc.data.vo.Region;
import org.isite.misc.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @Author <font color='blue'>zhangcm</font>
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
    @Cached(name = CacheKeys.REGION_LIST_PREFIX, key = "#pid", cacheType = CacheType.BOTH,
            expire = Constants.DAY_SECOND, localExpire = Constants.MINUTE_SECOND)
    public List<Region> getRegionByPid(int pid) {
        String fullName = regionService.getFullName(pid);
        return regionService.findByPid(pid, ActiveStatus.ENABLED).stream().map(regionPo ->
                        RegionConverter.toRegion(regionPo, fullName + regionPo.getRegionName()))
                .collect(Collectors.toList());
    }
}
