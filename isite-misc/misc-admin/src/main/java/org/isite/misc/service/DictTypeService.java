package org.isite.misc.service;

import org.isite.misc.mapper.DictTypeMapper;
import org.isite.misc.po.DictTypePo;
import org.isite.mybatis.service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 字典类型Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DictTypeService extends PoService<DictTypePo, Integer> {

    private DictDataService dictDataService;

    @Autowired
    public DictTypeService(DictTypeMapper dictTypeMapper) {
        super(dictTypeMapper);
    }

    /**
     * 删除字典类型和数据
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteDictType(String type) {
        dictDataService.deleteByType(type);
        DictTypePo typePo = new DictTypePo();
        typePo.setValue(type);
        return super.delete(typePo);
    }

    @Autowired
    public void setDictDataService(DictDataService dictDataService) {
        this.dictDataService = dictDataService;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(DictTypePo typePo, String oldValue) {
        dictDataService.updateDictType(typePo.getValue(), oldValue);
        return super.updateById(typePo);
    }
}
