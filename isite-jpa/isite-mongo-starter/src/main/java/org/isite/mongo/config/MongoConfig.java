package org.isite.mongo.config;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.mongo.converter.MongoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;
/**
 * @Description
 * 添加@EnableMongoAuditing注解使@CreatedDate和@LastModifiedDate生效.
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig implements ApplicationListener<ContextRefreshedEvent> {
    private static final String TYPEKEY = "_class";
    private final List<Converter<?, ?>> converters = new ArrayList<>();

    /**
     * 在构造函数中先获取所有实现了MongoConverter接口的bean，然后在@Bean方法中将这些bean注册为自定义的mongo转换器
     */
    @Autowired
    public MongoConfig(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(MongoConverter.class).forEach(
                (key, value) -> CollectionUtils.addAll(converters, value.conversions()));
    }

    /**
     * 注册自定义的mongo转换器
     */
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return MongoCustomConversions.create(adapter -> adapter.registerConverters(converters));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        MongoTemplate mongoTemplate = event.getApplicationContext().getBean(MongoTemplate.class);
        MappingMongoConverter converter = TypeUtils.cast(mongoTemplate.getConverter());
        if (converter.getTypeMapper().isTypeKey(TYPEKEY)) {
            //mongo插入一行数据时，会默认添加一个_class字段来存储实体类类型。设置取消_class字段
            converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        }
    }

    /**
     * 从版本4开始，MongoDB支持事务
     * MongoTransactionManager可以让应用程序使用 Spring的事务托管功能。
     */
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
