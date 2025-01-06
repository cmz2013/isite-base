package org.isite.log.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
@Document(indexName = "log")
public class LogPo {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String serviceId;

    @Field(type = FieldType.Long)
    private Long userId;
}