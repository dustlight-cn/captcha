package cn.dustlight.captcha.verifier;

import cn.dustlight.captcha.core.Code;

import java.util.Map;

/**
 * 验证码校验器
 */
public interface CodeVerifier<T> {

    void verify(Code<T> code, T target, Map<String, Object> parameters) throws VerifyCodeException;
}
