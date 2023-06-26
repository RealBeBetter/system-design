package com.example.commonutil.spring;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author wei.song
 * @since 2023/6/26 19:20
 */
public class SpringSpelExpressionUtil {

    /**
     * 解析Key键
     *
     * @param parameterNames 参数名称
     * @param args           arg参数
     * @param key            Key值
     * @return {@link Object}
     */
    public static Object parseSpelKey(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context);
    }

}
