package org.isite.jpa.service;

import lombok.Getter;
import org.isite.commons.lang.Functions;
import org.isite.jpa.data.BuiltIn;
import org.isite.jpa.data.Model;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.jpa.data.JpaConstants.INTERNAL_DATA_ILLEGAL_OPERATE;

/**
 * @Description 在增删改操作时，是否开启事务取决于数据库的默认行为和当前的会话设置。
 * 为确保事务的完整性，显式地使用事务控制语句，默认的REQUIRED 事务传播行为：如果当前存在一个事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
 * 使用事务时，要须控制事务粒度（增删改操作间隔时间），避免长事务锁表
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public abstract class BaseService<P extends Model<I>, I, N extends Number> {
    /**
     * PO Class
     */
    private final Class<P> poClass;

    protected BaseService() {
        this.poClass = initPoClass();
    }

    /**
     * 如果BaseService的泛型参数<P>,在子类依然使用泛型参数，则需要重新该方法
     */
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(this.getClass(), BaseService.class));
    }

    /**
     * 新增数据，若新增数据的主键已经存在，则会抛出异常。
     */
    @Transactional(rollbackFor = Exception.class)
    public N insert(P po) {
        if (po instanceof BuiltIn) {
            checkBuiltInData((BuiltIn) po);
        }
        return doInsert(po);
    }

    protected abstract N doInsert(P po);

    /**
     * 根据ID更新数据（包括空字段），不更新createTime
     */
    @Transactional(rollbackFor = Exception.class)
    public N updateById(P po) {
        if (po instanceof BuiltIn) {
            checkBuiltInData(po.getId(), ((BuiltIn) po).getInternal());
        }
        return doUpdateById(po);
    }

    protected abstract N doUpdateById(P po);

    /**
     * 根据ID更新非null字段，不更新createTime
     */
    @Transactional(rollbackFor = Exception.class)
    public N updateSelectiveById(P po) {
        if (po instanceof BuiltIn) {
            BuiltIn builtIn = (BuiltIn) po;
            if (null != builtIn.getInternal()) {
                checkBuiltInData(po.getId(), builtIn.getInternal());
            }
        }
        return doUpdateSelectiveById(po);
    }

    /**
     * 根据id查询数据，检查internal字段是否被更新
     */
    protected void checkBuiltInData(I id, Boolean internal) {
        BuiltIn oldPo = cast(this.get(id));
        notNull(oldPo, "id not found: " + id);
        isTrue(Objects.equals(oldPo.getInternal(), internal), INTERNAL_DATA_ILLEGAL_OPERATE);
    }

    /**
     * 检查po是否为内置数据
     */
    protected void checkBuiltInData(BuiltIn builtIn) {
        isTrue(null == builtIn.getInternal() || FALSE.equals(builtIn.getInternal()),
                INTERNAL_DATA_ILLEGAL_OPERATE);
    }

    /**
     * 根据id查询数据，并检查是否为内置数据
     */
    protected void checkBuiltInData(I id) {
        BuiltIn oldPo = cast(this.get(id));
        notNull(oldPo, "id not found: " + id);
        isTrue(null == oldPo.getInternal() || FALSE.equals(oldPo.getInternal()),
                INTERNAL_DATA_ILLEGAL_OPERATE);
    }

    protected abstract N doUpdateSelectiveById(P po);

    /**
     * 根据非空字段查询数据
     */
    public abstract List<P> findList(P po);

    /**
     * 查询所有数据
     */
    public abstract List<P> findAll();

    /**
     * 根据非null字段统计条数
     */
    public abstract N count(P po);

    /**
     * 统计条数
     */
    public abstract N countAll();

    /**
     * 统计条数
     */
    public abstract N count(Functions<P, Object> getter, Object value);

    /**
     * 根据ID查询数据
     */
    public abstract P get(I id);

    /**
     * 根据非空字段唯一索引查询数据
     */
    public abstract P findOne(P po);

    /**
     * 根据唯一索引查询数据
     */
    public abstract P findOne(Functions<P, Object> getter, Object value);

    /**
     * 查询数据，返回结果集要求不超过1000条
     */
    public abstract List<P> findList(Functions<P, Object> getter, Object value);

    /**
     * 判断数据是否存在
     */
    public abstract boolean exists(Functions<P, Object> getter, Object value);

    /**
     * 判断数据是否存在，排除指定ID
     */
    public abstract boolean exists(Functions<P, Object> getter, Object value, I exceptId);

    /**
     * 根据非空字段判断数据是否存在
     */
    public abstract boolean exists(P po);

    /**
     * 根据非空字段判断数据是否存在，排除指定ID
     */
    public abstract boolean exists(P po, I exceptId);

    /**
     * 根据主键删除数据
     */
    @Transactional(rollbackFor = Exception.class)
    public N delete(I id) {
        if (BuiltIn.class.isAssignableFrom(getPoClass())) {
            checkBuiltInData(id);
        }
        return doDelete(id);
    }

    protected abstract N doDelete(I id);

    /**
     * @Description 使用in条件查询。
     * NOT IN 子句有时可能会导致性能问题，特别是在子查询返回大量数据时。
     * 在这种情况下，可以考虑使用 LEFT JOIN 和 IS NULL 来替代 NOT IN，因为这种替代方法在某些情况下可能会更高效。
     * @param getter get方法
     * @param values 查询条件，values的元素个数不宜太多，避免一次查询条件过长导致数据库性能下降
     * @return 查询结果
     */
    public abstract List<P> findIn(Functions<P, Object> getter, Collection<?> values);

    /**
     * 使用in条件统计条数
     */
    public abstract N countIn(Functions<P, Object> getter, Collection<?> values);
}
