package cn.dustlight.captcha.sender;

import cn.dustlight.captcha.core.Code;

import java.util.Map;

/**
 * 验证码发送器
 */
public interface CodeSender<T> {

    void send(Code<T> code, Map<String, Object> parameters) throws SendCodeException;
}
