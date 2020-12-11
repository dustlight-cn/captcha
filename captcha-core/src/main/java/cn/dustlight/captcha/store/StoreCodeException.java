package cn.dustlight.captcha.store;

import cn.dustlight.captcha.CaptchaException;

public class StoreCodeException extends CaptchaException {

    public StoreCodeException(String message) {
        super(message);
    }

    public StoreCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
