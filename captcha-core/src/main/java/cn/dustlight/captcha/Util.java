package cn.dustlight.captcha;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    /* ===================================================================================================== */

    public static class AnnotationFieldFinder<T extends Annotation> {

        private ConcurrentHashMap<Class<?>, Vector<AnnotationField<T>>> cache;
        private Class<T> annotationClazz;

        private final static ConcurrentHashMap<Class<?>, AnnotationFieldFinder> finders = new ConcurrentHashMap<>();

        public static <T extends Annotation> AnnotationFieldFinder<T> get(Class<T> annotationClazz) {
            if (finders.containsKey(annotationClazz))
                return finders.get(annotationClazz);
            else {
                AnnotationFieldFinder<T> finder = new AnnotationFieldFinder<>(annotationClazz);
                finders.put(annotationClazz, finder);
                return finder;
            }
        }

        private AnnotationFieldFinder(Class<T> targetClazz) {
            this.cache = new ConcurrentHashMap<>();
            this.annotationClazz = targetClazz;
        }

        public Vector<AnnotationField<T>> find(Class<?> clazz) {
            if (clazz == null)
                return null;
            if (cache.containsKey(clazz))
                return cache.get(clazz);
            else {
                Vector<AnnotationField<T>> val = doProcess(clazz);
                cache.put(clazz, val);
                return val;
            }
        }

        protected Vector<AnnotationField<T>> doProcess(Class<?> clazz) {
            Vector<AnnotationField<T>> results = new Vector<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                T a;
                if ((a = field.getAnnotation(annotationClazz)) != null) {
                    AnnotationField<T> annotationField = new AnnotationField<>();
                    annotationField.setAnnotation(a);
                    annotationField.setField(field);
                    results.add(annotationField);
                }
            }
            return results;
        }
    }

    public static class AnnotationField<T extends Annotation> {

        private T annotation;
        private Field field;

        public T getAnnotation() {
            return annotation;
        }

        public void setAnnotation(T annotation) {
            this.annotation = annotation;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Object read(Object instance) {
            if (instance == null)
                return null;
            return new BeanWrapperImpl(instance).getPropertyValue(field.getName());
        }

        public void write(Object instance, Object value) {
            if (instance == null)
                return;
            new BeanWrapperImpl(instance).setPropertyValue(field.getName(), value);
        }
    }
}
