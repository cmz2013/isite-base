package org.isite.security.code;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.security.data.enums.CodeMode;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.Constants.TEN;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.schedule.RandomScheduler.nextInt;
import static org.isite.security.constants.SecurityConstants.VERIFY_CODE_LENGTH;

/**
 * @Description 验证码操作接口
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class CodeHandler implements Strategy<CodeMode> {
    protected static final String FIELD_CODE = "code";
    protected static final String FIELD_VALIDITY = "validity";
    private final CodeMode mode;
    private CodeCache codeCache;

    protected CodeHandler(CodeMode mode) {
        this.mode = mode;
    }

    /**
     * 校验验证码
     */
    public boolean checkCode(String agent, String code) {
        String cacheCode = codeCache.getCode(mode, agent);
        if (isNotBlank(code) && code.equals(cacheCode)) {
            //验证码只能使用一次
            codeCache.deleteCode(mode, agent);
            return TRUE;
        }
        return FALSE;
    }

    /**
     * 发送验证码（验证码有效期内不能重复发送）
     * @param agent 手机号或email地址等
     */
    public void sendCode(String agent) {
        String code = codeCache.getCode(mode, agent);
        if (isNotBlank(code)) {
            return;
        }
        code = generateCode();
        if (sendCode(agent, code)) {
            codeCache.saveCode(mode, agent, code);
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
    public void setCodeCache(CodeCache codeCache) {
        this.codeCache = codeCache;
    }
}
