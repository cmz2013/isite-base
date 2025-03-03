package org.isite.mongo.converter;

import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.commons.lang.enums.Enumerable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ActiveStatusConverter extends MongoConverter<ActiveStatus, Integer> {

    @Override
    protected Converter<Integer, ActiveStatus> createReadingConverter() {
        return new ActiveStatusReading();
    }

    @Override
    protected Converter<ActiveStatus, Integer> createWritingConverter() {
        return new ActiveStatusWriting();
    }

    @ReadingConverter
    public static class ActiveStatusReading implements Converter<Integer, ActiveStatus> {
        @Override
        public ActiveStatus convert(Integer code) {
            return Enumerable.getByCode(ActiveStatus.class, code);
        }
    }

    @WritingConverter
    public static class ActiveStatusWriting implements Converter<ActiveStatus, Integer> {
        @Override
        public Integer convert(ActiveStatus status) {
            return status.getCode();
        }
    }
}
