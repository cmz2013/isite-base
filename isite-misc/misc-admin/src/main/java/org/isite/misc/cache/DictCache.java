package org.isite.misc.cache;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import org.isite.misc.data.dto.DictDataDto;
import org.isite.misc.data.vo.DictData;
import org.isite.misc.po.DictDataPo;
import org.isite.misc.po.DictTypePo;
import org.isite.misc.service.DictDataService;
import org.isite.misc.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.alicp.jetcache.anno.CacheType.BOTH;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.data.Constants.DAY_SECONDS;
import static org.isite.commons.lang.data.Constants.MINUTE_SECONDS;
import static org.isite.misc.data.constants.CacheKey.DICT_TYPE_PREFIX;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Component
public class DictCache {

    private DictTypeService dictTypeService;
    private DictDataService dictDataService;

    /**
     * 根据字典类型查询字典数据，启用二级（远程+本地）缓存
     * name会被用于缓存key的前缀
     * 使用SpEL指定key
     */
    @Cached(name = DICT_TYPE_PREFIX, key = "#type", cacheType = BOTH, expire = DAY_SECONDS, localExpire = MINUTE_SECONDS)
    public List<DictData> findByType(String type) {
        return convert(dictDataService.findList(DictDataPo::getType, type), DictData::new);
    }

    /**
     * 新增字典数据
     */
    @CacheInvalidate(name = DICT_TYPE_PREFIX, key = "#dataDto.type")
    public int addDictData(DictDataDto dataDto) {
        return dictDataService.insert(convert(dataDto, DictDataPo::new));
    }

    /**
     * 删除字典数据，删除缓存
     */
    @CacheInvalidate(name = DICT_TYPE_PREFIX, key = "#po.type")
    public int deleteDictData(DictDataPo po) {
        return dictDataService.delete(po.getId());
    }

    /**
     * 更新字典数据，删除缓存
     */
    @CacheInvalidate(name = DICT_TYPE_PREFIX, key = "#dataDto.type")
    public int updateDictData(DictDataDto dataDto) {
        return dictDataService.updateById(convert(dataDto, DictDataPo::new));
    }

    /**
     * 更新字典类型，删除缓存
     */
    @CacheInvalidate(name = DICT_TYPE_PREFIX, key = "#oldValue")
    public int updateDictType(DictTypePo typePo, String oldValue) {
        return dictTypeService.updateDictType(typePo, oldValue);
    }

    /**
     * 删除字典类型和数据，释放缓存
     */
    @CacheInvalidate(name = DICT_TYPE_PREFIX, key = "#type")
    public int deleteDictType(String type) {
        return dictTypeService.deleteDictType(type);
    }

    @Autowired
    public void setDictTypeService(DictTypeService dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    @Autowired
    public void setDictDataService(DictDataService dictDataService) {
        this.dictDataService = dictDataService;
    }
}
