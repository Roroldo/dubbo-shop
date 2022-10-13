package com.topband.shop.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ParseElUtils
 * @packageName: com.topband.shop.utils
 * @description: 解析 el 表达式工具类
 * @date 2022/9/17 20:13
 */
public class ParseElUtils {
    public static Object parseElKey(String keyEl, String[] parameterNames, Object[] args) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(keyEl);
        return expression.getValue(getEvaluationContext(parameterNames, args));
    }

    private static StandardEvaluationContext getEvaluationContext(String[] parameterNames, Object[] args) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            // 设置参数
            context.setVariable(parameterNames[i], args[i]);
        }
        return context;
    }
}
