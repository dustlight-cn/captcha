package cn.dustlight.captcha.generator;

import cn.dustlight.captcha.core.Code;

import java.util.Map;

/**
 * 验证码生成器
 */
public interface CodeGenerator<T> {

    Code<T> generate(String name, Map<String, Object> parameters) throws GenerateCodeException;
}
