package org.isite.mongo.converter;

import org.isite.commons.lang.enums.ActiveStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.enums.Enumerable.getByCode;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SwitchStatusConverter extends MongoConverter<ActiveStatus, Integer> {

    @Override
    protected Converter<Integer, ActiveStatus> createReadingConverter() {
        return new SwitchStatusReading();
    }

    @Override
    protected Converter<ActiveStatus, Integer> createWritingConverter() {
        return new SwitchStatusWriting();
    }

    @ReadingConverter
    public static class SwitchStatusReading implements Converter<Integer, ActiveStatus> {
        @Override
        public ActiveStatus convert(Integer code) {
            return getByCode(ActiveStatus.class, code);
        }
    }

    @WritingConverter
    public static class SwitchStatusWriting implements Converter<ActiveStatus, Integer> {
        @Override
        public Integer convert(ActiveStatus status) {
            return status.getCode();
        }
    }
}
