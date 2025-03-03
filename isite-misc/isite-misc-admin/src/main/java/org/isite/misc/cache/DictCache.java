package org.isite.misc.cache;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.misc.converter.DictDataConverter;
import org.isite.misc.data.constants.CacheKeys;
import org.isite.misc.data.dto.DictDataDto;
import org.isite.misc.data.vo.DictData;
import org.isite.misc.po.DictDataPo;
import org.isite.misc.po.DictTypePo;
import org.isite.misc.service.DictDataService;
import org.isite.misc.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author <font color='blue'>zhangcm</font>
 */
@Component
public class DictCache {

    private DictTypeService dictTypeService;
    private DictDataService dictDataService;

    /**
     * 根据字典类型查询字典数据，启用二级（远程+本地）缓存。
     * 注解@Cached的name和key属性不能同时为空，name会被用于缓存key的前缀，使用SpEL指定key
     */
    @Cached(name = CacheKeys.DICT_TYPE_PREFIX, key = "#type", cacheType = CacheType.BOTH,
            expire = Constants.DAY_SECOND, localExpire = Constants.MINUTE_SECOND)
    public List<DictData> findByType(String type) {
        return DataConverter.convert(dictDataService.findList(DictDataPo::getType, type), DictData::new);
    }

    /**
     * 新增字典数据
     */
    @CacheInvalidate(name = CacheKeys.DICT_TYPE_PREFIX, key = "#dictDataDto.type")
    public int addDictData(DictDataDto dictDataDto) {
        return dictDataService.insert(DictDataConverter.toDictDataPo(dictDataDto));
    }

    /**
     * 删除字典数据，删除缓存
     */
    @CacheInvalidate(name = CacheKeys.DICT_TYPE_PREFIX, key = "#po.type")
    public int deleteDictData(DictDataPo po) {
        return dictDataService.delete(po.getId());
    }

    /**
     * 更新字典数据，删除缓存
     */
    @CacheInvalidate(name = CacheKeys.DICT_TYPE_PREFIX, key = "#dataDto.type")
    public int updateDictData(DictDataDto dataDto) {
        return dictDataService.updateById(DataConverter.convert(dataDto, DictDataPo::new));
    }

    /**
     * 更新字典类型，删除缓存
     */
    @CacheInvalidate(name = CacheKeys.DICT_TYPE_PREFIX, key = "#oldValue")
    public int updateDictType(DictTypePo typePo, String oldValue) {
        return dictTypeService.updateDictType(typePo, oldValue);
    }

    /**
     * 删除字典类型和数据，释放缓存
     */
    @CacheInvalidate(name = CacheKeys.DICT_TYPE_PREFIX, key = "#type")
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
