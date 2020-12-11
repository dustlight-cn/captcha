package cn.dustlight.captcha.sender;

import cn.dustlight.captcha.CaptchaException;

public class SendCodeException extends CaptchaException {

    public SendCodeException(String message) {
        super(message);
    }

    public SendCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
