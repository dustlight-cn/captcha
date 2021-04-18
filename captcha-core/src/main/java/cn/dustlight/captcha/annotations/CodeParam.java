package cn.dustlight.captcha.annotations;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 验证码参数
 * <p>
 * 验证参数用于在生产端向消费端传递消息。
 * 例如邮箱验证业务时，消费验证码成功后必须同时获取生产验证码时对应的邮箱。
 * 那么只需在生产端对邮箱参数添加此注解，如： sendEmailCode(@RequestParam @CodeParam("email") String email)
 * 在消费端时添加对应的参数即可获取注册邮箱：verifyEmail(@RequestParam @CodeValue String code, @CodeParam("email") String email)
 * </p>
 */
public @interface CodeParam {
    /**
     * 验证码名称，用于区分不同业务的验证码。
     *
     * @return
     */
    String value() default "code";

    /**
     * 参数名
     *
     * @return
     */
    String name() default "";

    /**
     * 默认参数值
     *
     * @return
     */
    String defaultValue() default "";
}
