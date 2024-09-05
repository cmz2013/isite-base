package org.isite.mongo.service;

import lombok.Getter;
import org.isite.commons.lang.Functions;
import org.isite.jpa.data.ListQuery;
import org.isite.jpa.data.OrderQuery;
import org.isite.jpa.data.Page;
import org.isite.jpa.data.PageQuery;
import org.isite.jpa.service.ModelService;
import org.isite.mongo.data.Po;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Reflection.toFieldName;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.jpa.converter.PageConverter.toPage;
import static org.isite.jpa.data.Direction.ASC;
import static org.isite.jpa.data.JpaConstants.FIELD_CREATE_TIME;
import static org.isite.jpa.data.JpaConstants.FIELD_ID;
import static org.isite.jpa.data.JpaConstants.FIELD_INTERNAL;
import static org.isite.jpa.data.JpaConstants.FIELD_UPDATE_TIME;
import static org.isite.mongo.converter.QueryConverter.toQuery;
import static org.isite.mongo.converter.QueryConverter.toQuerySelective;
import static org.isite.mongo.converter.UpdateConverter.toUpdate;
import static org.isite.mongo.converter.UpdateConverter.toUpdateSelective;
import static org.springframework.data.domain.Sort.Direction.valueOf;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class PoService<P extends Po<I>, I> extends ModelService<P, I, Long> {
    /**
     * mongo查询模板
     */
    private MongoTemplate mongoTemplate;

    public PoService() {
        super();
    }

    @Override
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(getClass(), PoService.class));
    }

    /**
     * 新增数据，若新增数据的主键已经存在，则会抛出异常
     * 1）mongoTemplate#save 若新增数据的主键已经存在，则会对当前已经存在的数据进行修改操作，包括空字段和createTime（如果createTime字段空，则更新为空）
     * 2）mongoTemplate#insert 若新增数据的主键已经存在，则会抛出异常
     * 3）po的 createTime和updateTime 字段分别添加了 @CreatedDate和@LastModifiedDate 注解，mongoTemplate#insert和save新增数据时会自动给它们赋值
     * 4）po的 updateTime 字段添加了@LastModifiedDate注解，mongoTemplate#save方法更新数据时会自动给 updateTime 赋值（createTime不赋值）
     */
    @Override
    protected Long doInsert(P po) {
        mongoTemplate.insert(po);
        return (long) ONE;
    }

    @Override
    protected Long doInsert(List<P> pos) {
        return (long) mongoTemplate.insert(pos, getPoClass()).size();
    }

    /**
     * 根据ID更新数据（包括空字段），不更新createTime
     * 注意：mongoTemplate#save方法会更新所有字段，包括空字段和createTime
     */
    @Override
    protected Long doUpdateById(P po) {
        return mongoTemplate.updateFirst(
                query(where(FIELD_ID).is(po.getId())),
                toUpdate(po, FIELD_ID, FIELD_CREATE_TIME, FIELD_UPDATE_TIME, FIELD_INTERNAL),
                getPoClass()).getModifiedCount();
    }

    /**
     * 根据ID更新非null字段，不更新createTime
     */
    @Override
    protected Long doUpdateSelectiveById(P po) {
        return mongoTemplate.updateFirst(
                query(where(FIELD_ID).is(po.getId())),
                toUpdateSelective(po, FIELD_ID, FIELD_CREATE_TIME, FIELD_UPDATE_TIME, FIELD_INTERNAL),
                getPoClass()).getModifiedCount();
    }

    @Override
    public Long count(P po) {
        return mongoTemplate.count(toQuerySelective(po), getPoClass());
    }

    @Override
    public Long countAll() {
        return mongoTemplate.count(new Query(), getPoClass());
    }

    @Override
    public Long count(Functions<P, Object> getter, Object value) {
        return mongoTemplate.count(toQuery(getter, value), getPoClass());
    }

    /**
     * 统计条数
     */
    public Long count(Query query) {
        return mongoTemplate.count(query, getPoClass());
    }

    @Override
    public P get(I id) {
        return mongoTemplate.findById(id, getPoClass());
    }

    @Override
    public P findOne(P po) {
        return mongoTemplate.findOne(toQuerySelective(po), getPoClass());
    }

    @Override
    public P findOne(Functions<P, Object> getter, Object value) {
        return mongoTemplate.findOne(toQuery(getter, value), getPoClass());
    }

    @Override
    public List<P> findList(Functions<P, Object> getter, Object value) {
        return mongoTemplate.find(toQuery(getter, value).limit(THOUSAND), getPoClass());
    }

    @Override
    public List<P> findList(P po) {
        return mongoTemplate.find(toQuerySelective(po), getPoClass());
    }

    @Override
    public List<P> findAll() {
        return mongoTemplate.find(new Query().limit(THOUSAND), getPoClass());
    }

    /**
     * 注意：如果仅仅只需要查一条数据，不要用分页方式，因为会统计总数
     */
    @Override
    public Page<P> findPage(PageQuery<P> pageQuery) {
        Query query = toQuerySelective(pageQuery.getPo());
        long total = this.count(query);
        query.limit(pageQuery.getPageSize()).skip(pageQuery.getOffset());
        if (isNotEmpty(pageQuery.getOrders())) {
            query.with(by(pageQuery.getOrders().stream().map(order -> new Sort.Order(
                    valueOf(order.getDirection().name()), order.getField())).collect(toList())));
        }
        return toPage(pageQuery, mongoTemplate.find(query, getPoClass()), total);
    }

    @Override
    public List<P> findList(ListQuery<P> listQuery) {
        OrderQuery order = listQuery.getOrder();
        Query query = null != listQuery.getPo() ?
                toQuerySelective(listQuery.getPo(), order.getField()) : new Query();
        if (null != listQuery.getIndex()) {
            Criteria criteria = new Criteria();
            if (ASC.equals(order.getDirection())) {
                criteria.and(order.getField()).gt(listQuery.getIndex());
            } else {
                criteria.and(order.getField()).lt(listQuery.getIndex());
            }
            query.addCriteria(criteria);
        }
        query.limit(listQuery.getPageSize()).skip(ZERO);
        query.with(by(valueOf(order.getDirection().name()), order.getField()));
        return mongoTemplate.find(query, getPoClass());
    }

    @Override
    public boolean exists(Functions<P, Object> getter, Object value) {
        return mongoTemplate.exists(toQuery(getter, value), getPoClass());
    }

    @Override
    public boolean exists(Functions<P, Object> getter, Object value, I exceptId) {
        return mongoTemplate.exists(toQuery(getter, value)
                .addCriteria(where(FIELD_ID).ne(exceptId)), getPoClass());
    }

    @Override
    public boolean exists(P po) {
        return mongoTemplate.exists(toQuerySelective(po), getPoClass());
    }

    @Override
    public boolean exists(P po, I exceptId) {
        return mongoTemplate.exists(toQuerySelective(po)
                .addCriteria(where(FIELD_ID).ne(exceptId)), getPoClass());
    }

    /**
     * 根据主键删除数据
     */
    @Override
    protected Long doDelete(I id) {
        return mongoTemplate.remove(query(where(FIELD_ID).is(id)), getPoClass()).getDeletedCount();
    }

    @Override
    protected Long doDelete(P po) {
        return mongoTemplate.remove(toQuerySelective(po), getPoClass()).getDeletedCount();
    }

    @Override
    protected Long doDelete(Functions<P, Object> getter, Object value) {
        return mongoTemplate.remove(toQuery(getter, value), getPoClass()).getDeletedCount();
    }

    @Override
    public List<P> findIn(Functions<P, Object> getter, Collection<?> values) {
        return mongoTemplate.find(query(new Criteria().and(toFieldName(getter)).in(values)), getPoClass());
    }

    @Override
    public Long countIn(Functions<P, Object> getter, Collection<?> values) {
        return mongoTemplate.count(query(new Criteria().and(toFieldName(getter)).in(values)), getPoClass());
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
