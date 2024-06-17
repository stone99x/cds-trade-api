package com.mars.cds.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectUtils {

    static final Logger log = LoggerFactory.getLogger(ReflectUtils.class.getSimpleName());

    /**
     * 方法存在，就返回方法实例
     */
    public static Method checkMethod(Class c, String methodName, Class... parameterTypes) {
        try {
            return c.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 对象转换(字段匹配上的转换)
     *
     * @param t 没有数据对象
     * @param r 数据对象
     */
    public static <T, R> void transform(T t, R r) {
        try {
            Class tc = t.getClass();
            Class rc = r.getClass();
            Field[] fields = rc.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                String name = f.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);

                String getMethodName = String.format("get%s", name); // get方法
                Method getMethod = rc.getDeclaredMethod(getMethodName);
                Object value = getMethod.invoke(r);

                String setMethodName = String.format("set%s", name); // set方法
                Method setMethod = checkMethod(tc, setMethodName, getMethod.getReturnType());

                if (setMethod != null) {
                    setMethod.invoke(t, value);
                }
            }
        } catch (Exception e) {
            LogUtils.error(log, "对象转换错误", e, r);
        }
    }
}
