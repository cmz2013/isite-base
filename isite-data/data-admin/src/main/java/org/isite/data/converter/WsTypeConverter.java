package org.isite.data.converter;

import org.isite.data.support.enums.WsType;
import org.isite.mongo.converter.MongoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.enums.Enumerable.getByCode;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WsTypeConverter extends MongoConverter<WsType, Integer> {

    @Override
    protected Converter<Integer, WsType> createReadingConverter() {
        return new WsTypeReading();
    }

    @Override
    protected Converter<WsType, Integer> createWritingConverter() {
        return new WsTypeWriting();
    }

    @ReadingConverter
    public static class WsTypeReading implements Converter<Integer, WsType> {
        @Override
        public WsType convert(Integer code) {
            return getByCode(WsType.class, code);
        }
    }

    @WritingConverter
    public static class WsTypeWriting implements Converter<WsType, Integer> {
        @Override
        public Integer convert(WsType wsType) {
            return wsType.getCode();
        }
    }
}
