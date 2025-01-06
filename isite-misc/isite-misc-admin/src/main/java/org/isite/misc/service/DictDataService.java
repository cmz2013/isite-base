package org.isite.misc.service;

import org.isite.misc.mapper.DictDataMapper;
import org.isite.misc.po.DictDataPo;
import org.isite.mybatis.service.PoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;

import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Description 字典数据Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class DictDataService extends PoService<DictDataPo, Integer> {

    @Autowired
    public DictDataService(DictDataMapper dictDataMapper) {
        super(dictDataMapper);
    }

    /**
     * 更新数据类型
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateDictType(String newType, String oldType) {
        DictDataPo dataPo = new DictDataPo();
        dataPo.setType(newType);
        Weekend<DictDataPo> weekend = of(DictDataPo.class);
        weekend.weekendCriteria().andEqualTo(DictDataPo::getType, oldType);
        return getMapper().updateByExampleSelective(dataPo, weekend);
    }

    /**
     * 根据字典类型删除字典数据
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByType(String type) {
        DictDataPo dataPo = new DictDataPo();
        dataPo.setType(type);
        return this.delete(dataPo);
    }
}
