package org.isite.misc.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.misc.converter.RegionConverter;
import org.isite.misc.mapper.RegionMapper;
import org.isite.misc.po.RegionPo;
import org.isite.mybatis.service.TreePoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @Description 地区Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class RegionService extends TreePoService<RegionPo, Integer> {

    @Autowired
    public RegionService(RegionMapper regionMapper) {
        super(regionMapper);
    }

    /**
     * 根据父节点ID查询地区
     */
    public List<RegionPo> findByPid(int pid, ActiveStatus status) {
        RegionPo regionPo = new RegionPo();
        regionPo.setPid(pid);
        regionPo.setStatus(status);
        return findList(regionPo);
    }

    /**
     * 查询地区完整名称
     */
    public String getFullName(int id) {
        if (isRoot(id)) {
            return Constants.BLANK_STR;
        }
        RegionPo regionPo = get(id);
        StringBuilder fullName = new StringBuilder();
        List<String> codes = RegionConverter.toPcodes(regionPo.getCode());
        if (CollectionUtils.isNotEmpty(codes)) {
            List<RegionPo> regions = findIn(RegionPo::getCode, codes);
            codes.forEach(code -> {
                for (RegionPo region : regions) {
                    if (code.equals(region.getCode())) {
                        fullName.append(region.getRegionName());
                        break;
                    }
                }
            });
        }
        return fullName.append(regionPo.getRegionName()).toString();
    }
}
