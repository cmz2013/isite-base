package org.isite.security.code;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.security.data.enums.VerifyCodeMode;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.data.Constants.TEN;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.schedule.RandomScheduler.nextInt;
import static org.isite.security.constants.SecurityConstants.VERIFY_CODE_LENGTH;

/**
 * @Description 验证码操作接口
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class VerifyCodeHandler implements Strategy<VerifyCodeMode> {
    protected static final String FIELD_CODE = "code";
    protected static final String FIELD_VALIDITY = "validity";
    private final VerifyCodeMode verifyCodeMode;
    private VerifyCodeCache verifyCodeCache;

    protected VerifyCodeHandler(VerifyCodeMode verifyCodeMode) {
        this.verifyCodeMode = verifyCodeMode;
    }

    /**
     * 校验验证码
     */
    public boolean checkCode(String agent, String code) {
        String cacheCode = verifyCodeCache.getCode(verifyCodeMode, agent);
        if (code.equals(cacheCode)) {
            //验证码只能使用一次
            verifyCodeCache.deleteCode(verifyCodeMode, agent);
            return TRUE;
        }
        return FALSE;
    }

    /**
     * 发送验证码（验证码有效期内不能重复发送）
     * @param agent 手机号或email地址等
     */
    public void sendCode(String agent) {
        String code = verifyCodeCache.getCode(verifyCodeMode, agent);
        if (isNotBlank(code)) {
            return;
        }
        code = generateCode();
        if (sendCode(agent, code)) {
            verifyCodeCache.saveCode(verifyCodeMode, agent, code);
        }
    }

    /**
     * 获取用户密保手机或Email
     * @param userSecret 用户秘钥信息
     * @return 用户密保终端
     */
    public abstract String getAgent(UserSecret userSecret);

    /**
     * 发送验证码
     * @param agent 用户密保终端
     * @param code 验证码
     * @return 是否发送成功
     */
    protected abstract boolean sendCode(String agent, String code);

    /**
     * 生成验证码
     */
    private String generateCode() {
        StringBuilder code = new StringBuilder();
        for (int i = ZERO; i < VERIFY_CODE_LENGTH; i++) {
            code.append(nextInt(TEN));
        }
        return code.toString();
    }

    @Autowired
    public void setVerifyCodeCache(VerifyCodeCache verifyCodeCache) {
        this.verifyCodeCache = verifyCodeCache;
    }
}
