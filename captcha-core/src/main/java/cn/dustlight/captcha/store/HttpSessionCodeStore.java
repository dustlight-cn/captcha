package cn.dustlight.captcha.store;

import cn.dustlight.captcha.CaptchaException;
import cn.dustlight.captcha.annotations.Duration;
import cn.dustlight.captcha.core.Code;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class HttpSessionCodeStore<T> implements CodeStore<T> {

    @Override
    public void store(Code<T> code, Duration duration, Map<String, Object> parameters) throws StoreCodeException {
        if (code == null || code.getName() == null)
            throw new StoreCodeException("Code is null");
        try {
            HttpSession session = getSession(true);
            if (duration != null && duration.enabled())
                session.setMaxInactiveInterval((int) (duration.value() / 1000));
            session.setAttribute(code.getName(), code);
        } catch (Exception e) {
            throw new StoreCodeException("Store code fail", e);
        }
    }

    @Override
    public Code<T> load(String name, Map<String, Object> parameters) throws LoadCodeException {
        try {
            HttpSession session = getSession(false);
            Object val = null;
            if (session == null || (val = session.getAttribute(name)) == null)
                throw new CodeNotExistsException("Code does not exists");
            return (Code) val;
        } catch (Exception e) {
            if (e instanceof CaptchaException)
                throw e;
            throw new LoadCodeException("Load code fail", e);
        }
    }

    @Override
    public void remove(String name) throws RemoveCodeException {
        try {
            HttpSession session = getSession(false);
            if (session == null)
                throw new CodeNotExistsException("Code does not exists");
            session.removeAttribute(name);
        } catch (Exception e) {
            if (e instanceof CaptchaException)
                throw e;
            throw new RemoveCodeException("Remove code fail", e);
        }
    }

    private HttpSession getSession(boolean createIfNull) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest().getSession(createIfNull);
    }
}
