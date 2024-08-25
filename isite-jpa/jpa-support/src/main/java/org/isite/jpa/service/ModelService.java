package org.isite.jpa.service;

import lombok.SneakyThrows;
import org.isite.commons.lang.Functions;
import org.isite.commons.lang.data.BuiltIn;
import org.isite.jpa.data.Model;
import org.isite.jpa.data.PageQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notEmpty;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Reflection.setValue;
import static org.isite.commons.lang.Reflection.toFieldName;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.jpa.data.Constants.INTERNAL_DATA_ILLEGAL_INSERTED;

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
        checkBuiltInData(pos);
        return doInsert(pos);
    }

    protected void checkBuiltInData(List<P> pos) {
        if (BuiltIn.class.isAssignableFrom(getPoClass())) {
            List<BuiltIn> builtInPos = cast(pos);
            builtInPos.forEach(po -> isTrue(null == po.getInternal() || FALSE.equals(po.getInternal()),
                        INTERNAL_DATA_ILLEGAL_INSERTED));
        }
    }

    protected abstract N doInsert(List<P> pos);

    /**
     * 根据ID更新某个字段
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public N updateById(I id, Functions<P, Object> getter, Object value) {
        checkBuiltInData(id);
        P po = getPoClass().getConstructor().newInstance();
        po.setId(id);
        setValue(po, toFieldName(getter), value);
        return doUpdateSelectiveById(po);
    }

    /**
     * 根据非空字段删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public N delete(P po) {
        // 系统内置数据禁止删除
        checkBuiltInData(po);
        return doDelete(po);
    }

    protected abstract N doDelete(P po);

    /**
     * 删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public N delete(Functions<P, Object> getter, Object value) {
        checkBuiltInData(getter, value);
        return doDelete(getter, value);
    }

    /**
     * @Description 根据getter字段检查是否存在内置数据。
     * 注解@SneakyThrows用于简化异常处理。它可以让方法在抛出受检异常时不需要显式地声明或捕获这些异常。
     */
    @SneakyThrows
    protected void checkBuiltInData(Functions<P, Object> getter, Object value) {
        if (BuiltIn.class.isAssignableFrom(getPoClass())) {
            P po = cast(getPoClass().getDeclaredConstructor().newInstance());
            ((BuiltIn) po).setInternal(TRUE);
            setValue(po, toFieldName(getter), value);
            isFalse(exists(po), INTERNAL_DATA_ILLEGAL_INSERTED);
        }
    }

    protected abstract N doDelete(Functions<P, Object> getter, Object value);

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
}
