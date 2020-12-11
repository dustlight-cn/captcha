package cn.dustlight.captcha;

public class CaptchaException extends RuntimeException {

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
