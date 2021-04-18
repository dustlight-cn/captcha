package cn.dustlight.captcha.core;

import cn.dustlight.captcha.Util;
import cn.dustlight.captcha.annotations.CodeParam;
import cn.dustlight.captcha.annotations.CodeValue;
import cn.dustlight.captcha.annotations.VerifyCode;
import cn.dustlight.captcha.configurations.DefaultBeanProperties;
import cn.dustlight.captcha.store.CodeStore;
import cn.dustlight.captcha.verifier.CodeVerifier;
import cn.dustlight.captcha.verifier.VerifyCodeException;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Vector;

public class VerifyCodeInterceptor implements MethodBeforeAdvice, Ordered {

    public static final String CHANCE_KEY = "CHANCE";
    private BeanFactory factory;
    private int order;
    private DefaultBeanProperties defaultBeanProperties;

    public VerifyCodeInterceptor(DefaultBeanProperties defaultBeanProperties) {
        this.defaultBeanProperties = defaultBeanProperties;
    }

    boolean checkChance(Code code, int chance) {
        if (code.getData().get(CHANCE_KEY) == null)
            return true;
        Integer CHANCE = Integer.valueOf(code.getData().get(VerifyCodeInterceptor.CHANCE_KEY).toString());
        if (CHANCE >= chance)
            return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    void addChanceCount(Code code) {
        if (code.getData().get(CHANCE_KEY) == null) {
            code.getData().put(CHANCE_KEY, 1);
            return;
        }
        Integer CHANCE = Integer.valueOf(code.getData().get(VerifyCodeInterceptor.CHANCE_KEY).toString());
        code.getData().put(VerifyCodeInterceptor.CHANCE_KEY, CHANCE + 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        VerifyCode verifyCodeAnnotation = AnnotationUtils.findAnnotation(method, VerifyCode.class); // 搜索注解
        /**
         * 获取Bean
         */
        CodeStore store = !StringUtils.hasText(verifyCodeAnnotation.store().value()) ?
                Util.getBean(factory, defaultBeanProperties.getStore().getName(), defaultBeanProperties.getStore().getType()) :
                Util.getBean(factory, verifyCodeAnnotation.store().value(), verifyCodeAnnotation.store().type());
        CodeVerifier verifier = !StringUtils.hasText(verifyCodeAnnotation.verifier().value()) ?
                Util.getBean(factory, defaultBeanProperties.getVerifier().getName(), defaultBeanProperties.getVerifier().getType()) :
                Util.getBean(factory, verifyCodeAnnotation.verifier().value(), verifyCodeAnnotation.verifier().type());

        Map<String, Object> parameters = Util.getParameters(method, objects); // 获取方法参数
        for (cn.dustlight.captcha.annotations.Parameter parameter : verifyCodeAnnotation.parameters())
            parameters.put(parameter.name(), parameter.value()); // 获取注解参数

        Code code = store.load(verifyCodeAnnotation.value(), parameters);
        Object codeValue = code.getValue();
        Object targetValue = parameters.get(verifyCodeAnnotation.value()); // 先取同名参数值为默认值，之后再扫描 @CodeValue 替代
        Parameter[] params = method.getParameters();

        if (params != null && objects != null) {
            for (int i = 0, len = Math.min(params.length, objects.length); i < len; i++) {
                CodeParam codeParamAnnotation;
                CodeValue codeValueAnnotation;
                if ((codeValueAnnotation = AnnotationUtils.findAnnotation(params[i], CodeValue.class)) != null &&
                        codeValueAnnotation.value().equals(verifyCodeAnnotation.value())) {
                    targetValue = objects[i];
                    objects[i] = codeValue; // 往验证码值注入方法参数
                    parameters.put(params[i].getName(), codeValue); // 更新参数表
                } else if ((codeParamAnnotation = AnnotationUtils.findAnnotation(params[i], CodeParam.class)) != null &&
                        codeParamAnnotation.value().equals(verifyCodeAnnotation.value())) {
                    String key = codeParamAnnotation.value().length() > 0 ? codeParamAnnotation.value() : params[i].getName();
                    Object value = code.getData() == null ? null : code.getData().get(key);
                    if (value == null && params[i].getType().isAssignableFrom(String.class))
                        value = codeParamAnnotation.defaultValue();
                    objects[i] = code.getData().getOrDefault(key, value); // 将验证码参数注入方法参数
                } else if (objects[i] != null) {
                    Class<?> paramType = params[i].getType();
                    Vector<Util.AnnotationField<CodeValue>> codeValues = Util.AnnotationFieldFinder.get(CodeValue.class).find(paramType);
                    Vector<Util.AnnotationField<CodeParam>> codeParams = Util.AnnotationFieldFinder.get(CodeParam.class).find(paramType);
                    if (codeValues != null)
                        for (Util.AnnotationField<CodeValue> field : codeValues) {
                            if (!field.getAnnotation().value().equals(verifyCodeAnnotation.value()))
                                continue;
                            Field f = field.getField();
                            targetValue = field.read(objects[i]);
                            field.write(objects[i], codeValue);
                            parameters.put(f.getName(), codeValue);
                        }
                    if (codeParams != null)
                        for (Util.AnnotationField<CodeParam> field : codeParams) {
                            if (!field.getAnnotation().value().equals(verifyCodeAnnotation.value()))
                                continue;
                            Field f = field.getField();
                            String key = field.getAnnotation().value().length() > 0 ? field.getAnnotation().value() : f.getName();
                            Object value = code.getData() == null ? null : code.getData().get(key);
                            if (value == null && f.getType().isAssignableFrom(String.class))
                                value = field.getAnnotation().defaultValue();
                            field.write(objects[i], code.getData().getOrDefault(key, value));
                        }
                }
            }
        }

        try {
            verifier.verify(code, targetValue, parameters);
            store.remove(verifyCodeAnnotation.value()); // 验证成功删除验证码
        } catch (VerifyCodeException e) {
            // 验证失败
            addChanceCount(code); // 记录++
            if (checkChance(code, verifyCodeAnnotation.delete().onFail())) {
                store.store(code, null, parameters);
            } else {
                store.remove(verifyCodeAnnotation.value()); // 重试次数用光
            }
            throw e;
        }
    }

    public BeanFactory getFactory() {
        return factory;
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
