package cn.dustlight.captcha.generator;

import cn.dustlight.captcha.CaptchaException;

public class GenerateCodeException extends CaptchaException {

    public GenerateCodeException(String message) {
        super(message);
    }

    public GenerateCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
