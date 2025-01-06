package org.isite.misc.converter;

import org.isite.misc.data.dto.TagCategoryDto;
import org.isite.misc.po.TagCategoryPo;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TagCategoryConverter {

    private TagCategoryConverter() {
    }

    public static TagCategoryPo toTagCategoryPo(TagCategoryDto tagCategoryDto) {
        TagCategoryPo tagCategoryPo = convert(tagCategoryDto, TagCategoryPo::new);
        if (null == tagCategoryPo.getRemark()) {
            tagCategoryPo.setRemark(BLANK_STR);
        }
        return tagCategoryPo;
    }
}
