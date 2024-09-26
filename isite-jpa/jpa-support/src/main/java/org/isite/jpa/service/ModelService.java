package org.isite.jpa.service;

import lombok.SneakyThrows;
import org.isite.commons.lang.Functions;
import org.isite.jpa.data.BuiltIn;
import org.isite.jpa.data.ListQuery;
import org.isite.jpa.data.Model;
import org.isite.jpa.data.PageQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.lang.Assert.notEmpty;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Reflection.setValue;
import static org.isite.commons.lang.Reflection.toFieldName;
import static org.isite.commons.lang.utils.TypeUtils.cast;

/**
 * @Description 返回结果集不超过1000条
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class ModelService<P extends Model<I>, I, N extends Number> extends BaseService<P, I, N> {

    protected ModelService() {
        super();
    }

    /**
     * 如果BaseService的泛型参数<P>,在子类依然使用泛型参数，则需要重新该方法
     */
    @Override
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(this.getClass(), ModelService.class));
    }

    /**
     * 批量新增数据，若新增数据的主键已经存在，则会抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public N insert(List<P> pos) {
        notEmpty(pos, "list cannot be empty");
        if (BuiltIn.class.isAssignableFrom(getPoClass())) {
            pos.forEach(po -> checkBuiltInData((BuiltIn) po));
        }
        return doInsert(pos);
    }

    protected abstract N doInsert(List<P> pos);

    /**
     * 根据ID更新某个字段
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public N updateById(I id, Functions<P, Object> getter, Object value) {
        P po = getPoClass().getConstructor().newInstance();
        po.setId(id);
        setValue(po, toFieldName(getter), value);
        if (BuiltIn.class.isAssignableFrom(getPoClass())) {
            checkBuiltInData((BuiltIn) po);
            checkBuiltInData(id);
        }
        return doUpdateSelectiveById(po);
    }

    /**
     * 根据非空字段删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public N delete(P po) {
        // 系统内置数据禁止删除
        if (po instanceof BuiltIn) {
            BuiltIn builtIn = (BuiltIn) po;
            checkBuiltInData(builtIn);
            if (null == builtIn.getInternal()) {
                builtIn.setInternal(FALSE);
            }
        }
        return doDelete(po);
    }

    protected abstract N doDelete(P po);

    /**
     * 删除数据
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public N delete(Functions<P, Object> getter, Object value) {
        P po = getPoClass().getConstructor().newInstance();
        setValue(po, toFieldName(getter), value);
        return this.delete(po);
    }

    /**
     * @Description 删除历史数据，插入新数据
     * @param getter PO条件字段Getter方法
     * @param value 条件字段值
     * @param pos 待插入的新数据
     */
    @Transactional(rollbackFor = Exception.class)
    public N deleteAndInsert(Functions<P, Object> getter, Object value, List<P> pos) {
        //自调用delete、insert，在运行时不会导致实际的事务
        this.delete(getter, value);
        return this.insert(pos);
    }

    /**
     * 根据非空字段分页查询数据，统计总条数
     */
    public abstract List<P> findPage(PageQuery<P> pageQuery);

    /**
     * 根据有序索引执行分页查询，不统计总条数
     */
    public abstract List<P> findList(ListQuery<P> listQuery);
}
