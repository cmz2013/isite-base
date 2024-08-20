package org.isite.jpa.service;

import lombok.Getter;
import org.isite.commons.lang.Functions;
import org.isite.commons.lang.data.BuiltIn;
import org.isite.jpa.data.Model;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.jpa.data.Constants.INTERNAL_DATA_ILLEGAL_DELETE;
import static org.isite.jpa.data.Constants.INTERNAL_DATA_ILLEGAL_INSERTED;
import static org.isite.jpa.data.Constants.INTERNAL_DATA_ILLEGAL_UPDATE;

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
        checkBuiltInDataInsert(po);
        return doInsert(po);
    }

    private void checkBuiltInDataInsert(P po) {
        // 系统内置数据只能数据库脚本插入，不允许使用该方法新增数据
        if (po instanceof BuiltIn) {
            BuiltIn builtIn = (BuiltIn) po;
            isTrue(null == builtIn.getInternal() || FALSE.equals(builtIn.getInternal()),
                    INTERNAL_DATA_ILLEGAL_INSERTED);
        }
    }

    protected abstract N doInsert(P po);

    /**
     * 根据ID更新数据（包括空字段），不更新createTime
     */
    @Transactional(rollbackFor = Exception.class)
    public N updateById(P po) {
        checkBuiltInDataUpdate(po);
        return doUpdateById(po);
    }

    protected abstract N doUpdateById(P po);

    /**
     * 根据ID更新非null字段，不更新createTime
     */
    @Transactional(rollbackFor = Exception.class)
    public N updateSelectiveById(P po) {
        checkBuiltInDataUpdate(po);
        return doUpdateSelectiveById(po);
    }

    /**
     * 系统内置数据只能数据库脚本更新，不允许使用该方法更新数据
     */
    private void checkBuiltInDataUpdate(P po) {
        if (po instanceof BuiltIn) {
            BuiltIn builtIn = (BuiltIn) po;
            isTrue(null == builtIn.getInternal() || FALSE.equals(builtIn.getInternal()),
                    INTERNAL_DATA_ILLEGAL_UPDATE);
            BuiltIn oldPo = (BuiltIn) this.get(po.getId());
            notNull(oldPo, "id not found: " + po.getId());
            isTrue(null == oldPo.getInternal() || FALSE.equals(oldPo.getInternal()),
                    INTERNAL_DATA_ILLEGAL_UPDATE);
        }
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
        checkBuiltInDataDelete(id);
        return doDelete(id);
    }

    private void checkBuiltInDataDelete(I id) {
        if (BuiltIn.class.isAssignableFrom(poClass)) {
            P oldPo = this.get(id);
            notNull(oldPo, "id not found: " + id);
            BuiltIn builtIn = (BuiltIn) oldPo;
            isTrue(null == builtIn.getInternal() || FALSE.equals(builtIn.getInternal()),
                    INTERNAL_DATA_ILLEGAL_DELETE);
        }
    }

    protected abstract N doDelete(I id);

    /**
     * 使用in条件查询
     * @param getter get方法
     * @param values 查询条件
     * @return 查询结果
     */
    public abstract List<P> findIn(Functions<P, Object> getter, Collection<?> values);
}
