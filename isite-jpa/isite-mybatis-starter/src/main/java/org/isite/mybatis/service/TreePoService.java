package org.isite.mybatis.service;

import org.apache.ibatis.session.RowBounds;
import org.isite.commons.lang.Functions;
import org.isite.jpa.service.TreeModelService;
import org.isite.mybatis.data.TreePo;
import org.isite.mybatis.mapper.TreePoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.weekend.Weekend;

import java.util.Collection;
import java.util.List;

import static com.github.pagehelper.page.PageMethod.offsetPage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.PERCENT;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Reflection.toFieldName;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TreePoService<P extends TreePo<I>, I> extends TreeModelService<P, I, Integer> {
    private TreePidsGenerator treePidsGenerator;
    /**
     * 通用mapper适合单表简单查询，复杂多表操作，建议自定义mapper，写SQL
     */
    private final TreePoMapper<P, I> mapper;

    public TreePoService(TreePoMapper<P, I> mapper) {
        super();
        this.mapper = mapper;
    }

    @Autowired
    public void setTreePidsGenerator(TreePidsGenerator treePidsGenerator) {
        this.treePidsGenerator = treePidsGenerator;
    }

    @Override
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(getClass(), TreePoService.class));
    }

    @Override
    protected Integer doInsert(P po) {
        po.setPids(getPids(po.getPid()));
        return mapper.insert(po);
    }

    /**
     * 根据ID更新数据（包括空字段），不更新createTime。如果pid变更，则同步更新所有子节点的pid
     */
    @Override
    protected Integer doUpdateById(P newPo) {
        P oldPo = get(newPo.getId());
        notNull(oldPo, "id not found: " + newPo.getId());
        int results = ZERO;
        if (!newPo.getPid().equals(oldPo.getPid())) {
            newPo.setPids(getPids(newPo.getPid()));
            results = updatePids(getPids(newPo), getPids(oldPo));
        }
        return results + mapper.updateByPrimaryKey(newPo);
    }

    /**
     * 根据ID更新非空字段，不更新createTime。如果pid变更，则同步更新所有子节点的pid
     */
    @Override
    protected Integer doUpdateSelectiveById(P newPo) {
        P oldPo = get(newPo.getId());
        notNull(oldPo, "id not found: " + newPo.getId());
        int results = ZERO;
        if (null != newPo.getPid() && !newPo.getPid().equals(oldPo.getPid())) {
            newPo.setPids(getPids(newPo.getPid()));
            results = updatePids(getPids(newPo), getPids(oldPo));
        }
        return results + mapper.updateByPrimaryKeySelective(newPo);
    }

    /**
     * 根据主键查询数据
     */
    @Override
    public P get(I id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 根据主键删除数据，并删除该分支节点下的所有节点
     */
    @Override
    protected Integer doDelete(I id) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andLike(P::getPids, getPids(id) + PERCENT);
        return mapper.deleteByPrimaryKey(id) + mapper.deleteByExample(weekend);
    }

    private int updatePids(String newPids, String oldPids) {
        return mapper.updatePids(treePidsGenerator.setPids(newPids, oldPids), oldPids + PERCENT);
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
    public List<P> findLikePids(String pids) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andLike(P::getPids, pids + PERCENT);
        return mapper.selectByExample(weekend);
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

    @Override
    public boolean exists(Functions<P, Object> getter, Object value) {
        return mapper.existsByColumn(toFieldName(getter), value);
    }

    @Override
    public boolean exists(Functions<P, Object> getter, Object value, I exceptId) {
        return mapper.existsByColumnAndExcludeId(toFieldName(getter), value, exceptId);
    }

    @Override
    public boolean exists(P po) {
        return mapper.existsByPo(po);
    }

    @Override
    public boolean exists(P po, I exceptId) {
        return mapper.existsByPoAndExcludeId(po, exceptId);
    }

    @Override
    public List<P> findIn(Functions<P, Object> getter, Collection<?> values) {
        return mapper.selectByExampleAndRowBounds(getWeekend(getter, values), new RowBounds(ZERO, THOUSAND));
    }

    @Override
    public Integer countIn(Functions<P, Object> getter, Collection<?> values) {
        return mapper.selectCountByExample(getWeekend(getter, values));
    }

    private Weekend<P> getWeekend(Functions<P, Object> getter, Collection<?> values) {
        Weekend<P> weekend = of(this.getPoClass());
        weekend.weekendCriteria().andIn(toFieldName(getter), values);
        return weekend;
    }

    protected TreePoMapper<P, I> getMapper() {
        return mapper;
    }
}
