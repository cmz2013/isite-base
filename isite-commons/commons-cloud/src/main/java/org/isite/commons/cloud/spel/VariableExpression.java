package org.isite.commons.cloud.spel;

import lombok.Getter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Description SpEL变量表达式
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class VariableExpression {

    private final StandardEvaluationContext evaluationContext;
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 使用参数名和参数值构造实例
     */
    public VariableExpression(String[] parameterNames, Object[] args) {
        this.evaluationContext = new StandardEvaluationContext(args);
        if (isNotEmpty(args)) {
            for (int i = ZERO; i < args.length; i++) {
                this.evaluationContext.setVariable(parameterNames[i], args[i]);
            }
        }
    }

    /**
     * 解析SpEL表达式返回值
     */
    public static Object getValue(String expression, String[] parameterNames, Object[] args) {
        notBlank(expression, "expression cannot be empty");
        return EXPRESSION_PARSER.parseExpression(expression)
                .getValue(new VariableExpression(parameterNames, args).getEvaluationContext());
    }
}
