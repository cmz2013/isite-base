package org.isite.commons.cloud.utils;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Description SpEL变量表达式工具类
 * @Author <font color='blue'>zhangcm</font>
 */
public class SpelExpressionUtils {

    private final StandardEvaluationContext evaluationContext;
    private final ExpressionParser expressionParser;

    /**
     * 使用参数名和参数值构造实例
     */
    public SpelExpressionUtils(String[] parameterNames, Object[] args) {
        this.evaluationContext = new StandardEvaluationContext(args);
        if (isNotEmpty(args)) {
            for (int i = ZERO; i < args.length; i++) {
                this.evaluationContext.setVariable(parameterNames[i], args[i]);
            }
        }
        this.expressionParser = new SpelExpressionParser();
    }

    /**
     * 解析SpEL表达式返回值
     */
    public Object getValue(String expression) {
        notBlank(expression, "expression cannot be empty");
        return expressionParser.parseExpression(expression).getValue(this.evaluationContext);
    }
}
