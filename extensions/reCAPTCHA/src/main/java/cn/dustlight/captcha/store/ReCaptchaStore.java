package cn.dustlight.captcha.store;

import cn.dustlight.captcha.ReCaptchaProperties;
import cn.dustlight.captcha.annotations.Duration;
import cn.dustlight.captcha.core.Code;
import cn.dustlight.captcha.core.DefaultCode;
import cn.dustlight.captcha.recaptcha.ReCaptchaResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ReCaptchaStore implements CodeStore<ReCaptchaResult> {

    private ReCaptchaProperties properties;
    private RestTemplate restTemplate;
    private HttpHeaders headers = new HttpHeaders();

    public ReCaptchaStore(ReCaptchaProperties properties) {
        this.properties = properties;
        restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    public void store(Code<ReCaptchaResult> code, Duration duration, Map<String, Object> parameters) throws StoreCodeException {
//        throw new StoreCodeException("ReCaptchaStore couldn't store code!");
    }

    @Override
    public Code<ReCaptchaResult> load(String name, Map<String, Object> parameters) throws LoadCodeException {
        try {
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String remoteIp = request.getRemoteAddr();

            map.add("remoteip", remoteIp);
            map.add("response", request.getParameter(parameters.getOrDefault("PARAM_NAME", properties.getDefaultParameterName()).toString()));
            map.add("secret", parameters.getOrDefault("SECRET", properties.getDefaultSecret()));
            HttpEntity<MultiValueMap<String, String>> data = new HttpEntity(map, headers);

            ReCaptchaResult result = getResult(data, properties.getEndpoint(), restTemplate);
            return new DefaultCode(result);
        } catch (Exception e) {
            if (e instanceof LoadCodeException)
                throw e;
            throw new LoadCodeException("ReCaptchaStore error on load!", e);
        }
    }

    @Override
    public void remove(String name) throws RemoveCodeException {
//        throw new RemoveCodeException("ReCaptchaStore couldn't remove code!");
    }

    protected ReCaptchaResult getResult(HttpEntity data, String url, RestTemplate template) {
        ReCaptchaResult result = template.postForEntity(url, data, ReCaptchaResult.class).getBody();
        return result;
    }
}
