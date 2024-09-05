package org.isite.mongo.service;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.isite.commons.lang.Functions;
import org.isite.jpa.service.TreeModelService;
import org.isite.mongo.data.TreePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.List;

import static com.mongodb.client.model.Filters.regex;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Constants.DOLLAR;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.jpa.data.JpaConstants.FIELD_CREATE_TIME;
import static org.isite.jpa.data.JpaConstants.FIELD_ID;
import static org.isite.jpa.data.JpaConstants.FIELD_PIDS;
import static org.isite.jpa.data.JpaConstants.FIELD_UPDATE_TIME;
import static org.isite.mongo.converter.QueryConverter.toQuery;
import static org.isite.mongo.converter.QueryConverter.toQuerySelective;
import static org.isite.mongo.converter.UpdateConverter.toUpdate;
import static org.isite.mongo.converter.UpdateConverter.toUpdateSelective;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class TreePoService<P extends TreePo<I>, I> extends TreeModelService<P, I, Long> {
    /**
     * mongo查询模板
     */
    private MongoTemplate mongoTemplate;

    public TreePoService() {
        super();
    }

    @Override
    protected Class<P> initPoClass() {
        return cast(getGenericParameter(getClass(), TreePoService.class));
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
        po.setPids(getPids(po.getPid()));
        mongoTemplate.insert(po);
        return (long) ONE;
    }

    /**
     * 根据ID更新数据（包括空字段），不更新createTime。如果pid变更，则同步更新所有子节点的pid
     * 注意：mongoTemplate#save方法会更新所有字段，包括空字段和createTime
     */
    @Override
    protected Long doUpdateById(P newPo) {
        P oldPo = get(newPo.getId());
        notNull(oldPo, "id not found: " + newPo.getId());
        long results = ZERO;
        if (!newPo.getPid().equals(oldPo.getPid())) {
            newPo.setPids(getPids(newPo.getPid()));
            results = updatePids(getPids(newPo), getPids(oldPo));
        }
        return results + mongoTemplate.updateFirst(
                query(where(FIELD_ID).is(newPo.getId())),
                toUpdate(newPo, FIELD_ID, FIELD_CREATE_TIME, FIELD_UPDATE_TIME),
                getPoClass()).getModifiedCount();
    }

    /**
     * 根据ID更新非空字段，不更新createTime。如果pid变更，则同步更新所有子节点的pid
     * 注意：mongoTemplate#save方法会更新所有字段，包括空字段和createTime
     */
    @Override
    protected Long doUpdateSelectiveById(P newPo) {
        P oldPo = get(newPo.getId());
        notNull(oldPo, "id not found: " + newPo.getId());
        long results = ZERO;
        if (null != newPo.getPid() && !newPo.getPid().equals(oldPo.getPid())) {
            newPo.setPids(getPids(newPo.getPid()));
            results = updatePids(getPids(newPo), getPids(oldPo));
        }
        return results + mongoTemplate.updateFirst(
                query(where(FIELD_ID).is(newPo.getId())),
                toUpdateSelective(newPo, FIELD_ID, FIELD_CREATE_TIME, FIELD_UPDATE_TIME),
                getPoClass()).getModifiedCount();
    }

    /**
     * @Description 更新指定分支节点下的所有节点的pids
     * MongoDB的Java驱动程序提供了Document类来表示文档，使用Document类的set方法来执行$set操作的等效操作
     * db.resource.updateMany(
     *     {"pids":{"$regex": "^1,6"}},
     *     [{"$set": {"pids": { "$concat": ["1,8", { "$substr": ["$pids", 4]}]}}}])
     */
    private Long updatePids(String newPids, String oldPids) {
        String collectionName = mongoTemplate.getCollectionName(getPoClass());
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        Bson filter = regex(FIELD_PIDS, "^" + oldPids);
        Bson update = new Document("$set", new Document(FIELD_PIDS,
                new Document("$concat", new Object[] {newPids, new Document("$substr",
                        new Object[] {DOLLAR + FIELD_PIDS, oldPids.length() + ONE})})));
        return collection.updateMany(filter, update).getModifiedCount();
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
    public List<P> findList(P po) {
        return mongoTemplate.find(toQuerySelective(po), getPoClass());
    }

    @Override
    public List<P> findAll() {
        return mongoTemplate.find(new Query().limit(THOUSAND), getPoClass());
    }

    @Override
    public List<P> findIn(Functions<P, Object> getter, Collection<?> values) {
        return mongoTemplate.find(toQuery(getter, values), getPoClass());
    }

    @Override
    public Long countIn(Functions<P, Object> getter, Collection<?> values) {
        return mongoTemplate.count(toQuery(getter, values), getPoClass());
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

    @Override
    public List<P> findList(Functions<P, Object> getter, Object value) {
        return mongoTemplate.find(toQuery(getter, value).limit(THOUSAND), getPoClass());
    }

    @Override
    public List<P> findLikePids(String pids) {
        return mongoTemplate.find(query(where(FIELD_PIDS).regex("^" + pids)), getPoClass());
    }

    /**
     * 根据主键删除数据，并删除该分支节点下的所有节点
     */
    @Override
    protected Long doDelete(I id) {
        return mongoTemplate.remove(query(where(FIELD_ID).is(id)), getPoClass()).getDeletedCount() +
                mongoTemplate.remove(query(where(FIELD_PIDS).regex("^" + getPids(id))), getPoClass()).getDeletedCount();
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
