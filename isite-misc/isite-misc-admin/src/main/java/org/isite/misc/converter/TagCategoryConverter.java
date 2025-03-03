package org.isite.misc.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.misc.data.dto.TagCategoryDto;
import org.isite.misc.po.TagCategoryPo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TagCategoryConverter {

    private TagCategoryConverter() {
    }

    public static TagCategoryPo toTagCategoryPo(TagCategoryDto tagCategoryDto) {
        TagCategoryPo tagCategoryPo = DataConverter.convert(tagCategoryDto, TagCategoryPo::new);
        if (null == tagCategoryPo.getRemark()) {
            tagCategoryPo.setRemark(Constants.BLANK_STR);
        }
        return tagCategoryPo;
    }
}
