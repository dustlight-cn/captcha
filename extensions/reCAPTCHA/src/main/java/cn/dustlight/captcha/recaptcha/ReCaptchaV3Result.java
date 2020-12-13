package cn.dustlight.captcha.recaptcha;

public class ReCaptchaV3Result extends ReCaptchaResult {

    private float score;

    private String action;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return super.toString() + ", V3{" +
                "score=" + score +
                ", action='" + action + '\'' +
                '}';
    }
}
