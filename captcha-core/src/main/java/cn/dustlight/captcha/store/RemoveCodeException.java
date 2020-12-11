package cn.dustlight.captcha.store;

import cn.dustlight.captcha.CaptchaException;

public class RemoveCodeException extends CaptchaException {

    public RemoveCodeException(String message) {
        super(message);
    }

    public RemoveCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
