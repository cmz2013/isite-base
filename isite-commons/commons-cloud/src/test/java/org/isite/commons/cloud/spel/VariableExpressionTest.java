package org.isite.commons.cloud.spel;

import lombok.Getter;
import lombok.Setter;

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
        System.out.println(VariableExpression.getValue("#prize.totalInventory >= 0", new String[]{"prize"}, new Object[]{prize}));
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
