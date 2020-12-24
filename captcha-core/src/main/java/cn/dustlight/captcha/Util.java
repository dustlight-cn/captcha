package cn.dustlight.captcha;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static <T> T getBean(BeanFactory factory, String name, Class<? extends T> clazz) {
        return factory.getBean(name, clazz);
    }

    public static Map<String, Object> getParameters(Method method, Object[] arguments) {
        String[] names;
        if (arguments == null || (names = parameterNameDiscoverer.getParameterNames(method)) == null)
            return Collections.EMPTY_MAP;
        Map<String, Object> parameters = new LinkedHashMap<>();
        for (int i = 0, len = Math.min(names.length, arguments.length); i < len; i++)
            parameters.put(names[i], arguments[i]);
        return parameters;
    }
}
