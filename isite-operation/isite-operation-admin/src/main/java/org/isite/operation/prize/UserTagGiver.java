package org.isite.operation.prize;

import org.isite.misc.data.dto.TagRecordDto;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.support.enums.PrizeType;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.isite.misc.client.TagRecordAccessor.addTagRecord;
import static org.isite.misc.data.enums.ObjectType.USER;
import static org.isite.operation.support.enums.PrizeType.USER_TAG;

/**
 * @Description 用户标签发放接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserTagGiver extends PrizeGiver {

    @Override
    protected void grantPrize(PrizeRecordPo prizeRecordPo) {
        TagRecordDto tagRecordDto = new TagRecordDto();
        tagRecordDto.setTagId(parseInt(prizeRecordPo.getThirdPrizeValue()));
        tagRecordDto.setObjectType(USER);
        tagRecordDto.setObjectValue(valueOf(prizeRecordPo.getUserId()));
        addTagRecord(tagRecordDto);
    }

    @Override
    public PrizeType[] getIdentities() {
        return new PrizeType[] { USER_TAG };
    }

}
