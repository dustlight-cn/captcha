package cn.dustlight.captcha.store;

import cn.dustlight.captcha.CaptchaException;
import cn.dustlight.captcha.annotations.Duration;
import cn.dustlight.captcha.core.Code;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisCodeStore<T> implements CodeStore<T>, InitializingBean {

    private String keyPrefix;
    private RedisTemplate<String, Code<T>> redisTemplate;

    public RedisCodeStore(RedisConnectionFactory factory, String keyPrefix) {
        this.keyPrefix = keyPrefix;
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(factory);
        this.redisTemplate.setKeySerializer(RedisSerializer.string());
        this.redisTemplate.setValueSerializer(RedisSerializer.java());
        this.redisTemplate.setHashKeySerializer(RedisSerializer.string());
        this.redisTemplate.setHashValueSerializer(RedisSerializer.java());
    }

    @Override
    public void store(Code<T> code, Duration duration, Map<String, Object> parameters) throws StoreCodeException {
        if (code == null || code.getName() == null)
            throw new StoreCodeException("Code is null");
        try {
            if (duration != null && duration.enabled())
                this.redisTemplate.opsForValue().set(key(code), code, duration.value(), TimeUnit.MILLISECONDS);
            else
                this.redisTemplate.opsForValue().set(key(code), code);
        } catch (Exception e) {
            throw new StoreCodeException("Store code fail", e);
        }
    }

    @Override
    public Code<T> load(String name, Map<String, Object> parameters) throws LoadCodeException {
        try {
            Code<T> val = this.redisTemplate.opsForValue().get(key(name));
            if (val == null || val.getValue() == null)
                throw new CodeNotExistsException("Code does not exists");
            return val;
        } catch (Exception e) {
            if (e instanceof CaptchaException)
                throw e;
            throw new LoadCodeException("Load code fail", e);
        }
    }

    @Override
    public void remove(String name) throws RemoveCodeException {
        try {
            if (!this.redisTemplate.delete(key(name)))
                throw new CodeNotExistsException("Code does not exists");
        } catch (Exception e) {
            if (e instanceof CaptchaException)
                throw e;
            throw new RemoveCodeException("Remove code fail", e);
        }
    }

    protected String key(Code<T> code) {
        return key(code.getName());
    }

    protected String key(String codeName) {
        HttpSession session = getSession(true);
        return String.format("%s:%s:%s", keyPrefix, session.getId(), codeName);
    }

    protected HttpSession getSession(boolean createIfNull) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest().getSession(createIfNull);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisTemplate.afterPropertiesSet();
    }
}
