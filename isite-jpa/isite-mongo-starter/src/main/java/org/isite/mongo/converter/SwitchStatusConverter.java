package org.isite.mongo.converter;

import org.isite.commons.lang.enums.SwitchStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.enums.Enumerable.getByCode;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SwitchStatusConverter extends MongoConverter<SwitchStatus, Integer> {

    @Override
    protected Converter<Integer, SwitchStatus> createReadingConverter() {
        return new SwitchStatusReading();
    }

    @Override
    protected Converter<SwitchStatus, Integer> createWritingConverter() {
        return new SwitchStatusWriting();
    }

    @ReadingConverter
    public static class SwitchStatusReading implements Converter<Integer, SwitchStatus> {
        @Override
        public SwitchStatus convert(Integer code) {
            return getByCode(SwitchStatus.class, code);
        }
    }

    @WritingConverter
    public static class SwitchStatusWriting implements Converter<SwitchStatus, Integer> {
        @Override
        public Integer convert(SwitchStatus status) {
            return status.getCode();
        }
    }
}
