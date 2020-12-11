package cn.dustlight.captcha.store;

import cn.dustlight.captcha.CaptchaException;

public class LoadCodeException extends CaptchaException {

    public LoadCodeException(String message) {
        super(message);
    }

    public LoadCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
