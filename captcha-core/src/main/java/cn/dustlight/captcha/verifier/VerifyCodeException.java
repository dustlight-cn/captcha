package cn.dustlight.captcha.verifier;

import cn.dustlight.captcha.CaptchaException;

public class VerifyCodeException extends CaptchaException {

    public VerifyCodeException(String message) {
        super(message);
    }

    public VerifyCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
