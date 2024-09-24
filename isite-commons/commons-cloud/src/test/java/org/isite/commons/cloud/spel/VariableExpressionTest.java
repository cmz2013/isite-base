package org.isite.commons.cloud.spel;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.utils.SpelExpressionUtils;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class VariableExpressionTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Prize prize = new Prize();
        prize.setTotalInventory(0);
        SpelExpressionUtils spelExpressionUtils = new SpelExpressionUtils(new String[]{"prize"}, new Object[]{prize});
        System.out.println(spelExpressionUtils.getValue("#prize.totalInventory >= 0"));
    }

    @Getter
    @Setter
    public static class Prize {
        private Integer activityId;
        private String prizeName;
        private String thirdPrizeValue;
        private String prizeImage;
        private Integer totalInventory;
        private Integer probability;
        private Integer consumeInventory;
        private String remark;
    }
}
