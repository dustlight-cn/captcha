package cn.dustlight.captcha.store;

import cn.dustlight.captcha.ReCaptchaProperties;
import cn.dustlight.captcha.recaptcha.ReCaptchaResult;
import cn.dustlight.captcha.recaptcha.ReCaptchaV3Result;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class ReCaptchaV3Store extends ReCaptchaStore {

    public ReCaptchaV3Store(ReCaptchaProperties properties) {
        super(properties);
    }

    @Override
    protected ReCaptchaResult getResult(Object data, String url, RestTemplate template) {
        ReCaptchaResult result = template.postForEntity(url, data, ReCaptchaV3Result.class).getBody();
        return result;
    }
}
