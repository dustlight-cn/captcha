package cn.dustlight.captcha.verifier;

import cn.dustlight.captcha.core.Code;
import cn.dustlight.captcha.recaptcha.ReCaptchaResult;

import java.util.Map;

public class ReCaptchaVerifier implements CodeVerifier<ReCaptchaResult> {

    @Override
    public void verify(Code<ReCaptchaResult> code, ReCaptchaResult target, Map<String, Object> parameters) throws VerifyCodeException {
        if (code == null || code.getValue() == null)
            throw new VerifyCodeException("Code is null!");
        if (!code.getValue().getSuccess())
            throw new VerifyCodeException("Fail to verify code: " + code.getValue().toString());
    }

}
