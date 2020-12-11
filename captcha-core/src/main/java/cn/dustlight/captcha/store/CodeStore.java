package cn.dustlight.captcha.store;

import cn.dustlight.captcha.annotations.Duration;
import cn.dustlight.captcha.core.Code;

import java.util.Map;

/**
 * 验证码储存器
 */
public interface CodeStore<T> {

    void store(Code<T> code, Duration duration, Map<String, Object> parameters) throws StoreCodeException;

    Code<T> load(String name, Map<String, Object> parameters) throws LoadCodeException;

    void remove(String name) throws RemoveCodeException;

}
