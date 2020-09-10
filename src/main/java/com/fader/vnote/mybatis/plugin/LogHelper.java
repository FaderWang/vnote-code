package com.fader.vnote.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Properties;

/**
 * @author FaderW
 * 2019/11/27
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                        Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
                        Object.class, RowBounds.class, ResultHandler.class}),
        }
)
public class LogHelper implements Interceptor{

    static {
        i = 20;
    }
    static int i = 10;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println("sql args " + invocation.getArgs());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static void main(String[] args) {
        System.out.println(i);
    }
}
