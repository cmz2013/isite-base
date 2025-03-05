package org.isite.operation.prize;

import org.isite.misc.client.TagRecordAccessor;
import org.isite.misc.data.dto.TagRecordDto;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.support.enums.PrizeType;
import org.springframework.stereotype.Component;
/**
 * @Description 用户标签发放接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserTagGiver extends PrizeGiver {

    @Override
    protected void grantPrize(PrizeRecordPo prizeRecordPo) {
        TagRecordDto tagRecordDto = new TagRecordDto();
        tagRecordDto.setTagId(Integer.parseInt(prizeRecordPo.getThirdPrizeValue()));
        tagRecordDto.setObjectType(ObjectType.USER);
        tagRecordDto.setObjectValue(String.valueOf(prizeRecordPo.getUserId()));
        TagRecordAccessor.addTagRecord(tagRecordDto);
    }

    @Override
    public PrizeType[] getIdentities() {
        return new PrizeType[] {PrizeType.USER_TAG};
    }
}
