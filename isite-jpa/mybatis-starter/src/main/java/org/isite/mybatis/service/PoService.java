package org.isite.mybatis.service;

import com.github.pagehelper.Page;
import org.apache.ibatis.session.RowBounds;
import org.isite.commons.lang.Functions;
import org.isite.jpa.data.ListQuery;
import org.isite.jpa.data.PageQuery;
import org.isite.jpa.service.ModelService;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.mapper.PoMapper;
import tk.mybatis.mapper.weekend.Fn;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.List;

import static com.github.pagehelper.page.PageMethod.offsetPage;
import static org.isite.commons.lang.Assert.notEmpty;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Reflection.toFieldName;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @param <P> PO
 * @param <I> 主键
 * @Author <font color='blue'>zhangcm</font>
 */
public class PoService<P extends Po<I>, I> extends ModelService<P, I, Integer> {
    /**
     * 通用mapper适合单表简单查询，复杂多表操作，建议自定义mapper，写SQL
     */
    private final PoMapper<P, I> mapper;

    public PoService(PoMapper<P, I> mapper) {
        super();
        this.mapper = mapper;
    }

    @Override
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(this.getClass(), PoService.class));
    }

    @Override
    protected Integer doInsert(P po) {
        return mapper.insert(po);
    }

    @Override
    protected Integer doInsert(List<P> pos) {
        return mapper.insertList(pos);
    }

    @Override
    protected Integer doUpdateById(P po) {
        return mapper.updateByPrimaryKey(po);
    }

    @Override
    protected Integer doUpdateSelectiveById(P po) {
        return mapper.updateByPrimaryKeySelective(po);
    }

    /**
     * 根据主键查询数据
     */
    @Override
    public P get(I id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    protected Integer doDelete(P po) {
        return mapper.delete(po);
    }

    @Override
    protected Integer doDelete(Functions<P, Object> getter, Object value) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andEqualTo(toFieldName(getter), value);
        return mapper.deleteByExample(weekend);
    }

    @Override
    protected Integer doDelete(I id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public P findOne(P po) {
        return mapper.selectOne(po);
    }

    @Override
    public P findOne(Functions<P, Object> getter, Object value) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andEqualTo(toFieldName(getter), value);
        return mapper.selectOneByExample(weekend);
    }

    @Override
    public List<P> findList(Functions<P, Object> getter, Object value) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andEqualTo(toFieldName(getter), value);
        return mapper.selectByExampleAndRowBounds(weekend, new RowBounds(ZERO, THOUSAND));
    }

    /**
     * 查询数据
     */
    public List<P> findList(Weekend<P> weekend) {
        return mapper.selectByExampleAndRowBounds(weekend, new RowBounds(ZERO, THOUSAND));
    }

    @Override
    public List<P> findList(P po) {
        return mapper.selectByRowBounds(po, new RowBounds(ZERO, THOUSAND));
    }

    @Override
    public List<P> findAll() {
        offsetPage(ZERO, THOUSAND);
        return mapper.selectAll();
    }

    @Override
    public boolean exists(P po) {
        return mapper.existsByPo(po);
    }

    @Override
    public boolean exists(P po, I excludeId) {
        return mapper.existsByPoAndExcludeId(po, excludeId);
    }

    @Override
    public boolean exists(Functions<P, Object> getter, Object value) {
        return mapper.existsByColumn(toFieldName(getter), value);
    }

    @Override
    public boolean exists(Functions<P, Object> getter, Object value, I excludeId) {
        return mapper.existsByColumnAndExcludeId(toFieldName(getter), value, excludeId);
    }

    /**
     * 根据非空字段统计条数
     */
    @Override
    public Integer count(P po) {
        return mapper.selectCount(po);
    }

    @Override
    public Integer countAll() {
        return mapper.selectCount(null);
    }

    @Override
    public Integer count(Functions<P, Object> getter, Object value) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andEqualTo(toFieldName(getter), value);
        return mapper.selectCountByExample(weekend);
    }

    /**
     * 统计条数
     */
    public Integer count(Weekend<P> weekend) {
        return mapper.selectCountByExample(weekend);
    }

    /**
     * 注意：如果仅仅只需要查一条数据，不要用分页方式，因为会统计总数
     */
    @Override
    public Page<P> findPage(PageQuery<P> pageQuery) {
        //当前线程紧跟着的第一个select方法会被分页
        offsetPage(pageQuery.getOffset(), pageQuery.getPageSize()).setOrderBy(pageQuery.orderBy());
        return (Page<P>) mapper.select(pageQuery.getPo());
    }

    /**
     * 根据顺序索引分页查询，不统计总条数
     * @param listQuery 查询条件
     * @param fn 顺序索引字段在PO中的get函数
     * @return 查询结果
     */
    public List<P> findList(Weekend<P> weekend, ListQuery listQuery, Fn<P, Object> fn) {
        return mapper.selectByExampleAndRowBounds(
                weekend.weekendCriteria().andGreaterThan(fn, listQuery.getMinIndex()),
                new RowBounds(ZERO, listQuery.getPageSize()));
    }

    /**
     * 使用in条件查询
     * @param getter get方法
     * @param values 查询条件
     * @return 查询结果
     */
    @Override
    public List<P> findIn(Functions<P, Object> getter, Collection<?> values) {
        notEmpty(values, "values cannot be empty");
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andIn(toFieldName(getter), values);
        return mapper.selectByExampleAndRowBounds(weekend, new RowBounds(ZERO, THOUSAND));
    }

    protected PoMapper<P, I> getMapper() {
        return mapper;
    }
}
