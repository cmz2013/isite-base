package org.isite.security.login;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.http.HttpClient;
import org.isite.commons.web.sign.SignSecret;
import org.isite.security.converter.UserConverter;
import org.isite.security.data.enums.CodeLoginMode;
import org.isite.security.data.vo.OauthUser;
import org.isite.user.client.UserAccessor;
import org.isite.user.data.constants.UserConstants;
import org.isite.user.data.dto.WechatPostDto;
import org.isite.user.data.enums.Sex;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
/**
 * @Description 微信登录流程如下：
 * step1: 在 微信开放平台 注册一个应用，并获取到 App ID 和 App Secret
 * step2: 在 登录页面引导用户访问微信的授权页面，获取用户的授权。
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect
 * step3: 微信服务器会将用户重定向到回调地址，并附带一个 code 参数，该参数是微信用户的唯一标识。
 * step4: 使用 code 和 App Secret 通过以下 URL 获取访问令牌和用户信息
 * GET https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code
 * step5: 根据之前获取到的 openid 查询微信手机号
 * POST https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s
 * step6: 根据微信手机号查询用户信息，如果查询不到用户信息时自动注册
 * GET https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN
 *
 * 登录页面完成step3，使用code请求/oauth/token接口，WechatTokenLogin负责step4和step5，完成登录。
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
@EnableConfigurationProperties(WechatProperties.class)
public class WechatLogin implements CodeLogin {
    private static final String FIELD_APP_ID = "appid";
    private static final String FIELD_SECRET = "secret";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_GRANT_TYPE = "grant_type";
    private static final String FIELD_ACCESS_TOKEN = "access_token";
    private static final String FIELD_OPENID = "openid";
    private static final String FIELD_PHONE_INFO = "phone_info";
    private static final String FIELD_PHONE_NUMBER = "phoneNumber";
    private static final String FIELD_NICKNAME = "nickname";
    private static final String FIELD_SEX = "sex";
    private static final String FIELD_HEADIMGURL = "headimgurl";

    private final WechatProperties wechatProperties;
    private SignSecret signSecret;

    @Autowired
    public WechatLogin(WechatProperties wechatProperties) {
        this.wechatProperties = wechatProperties;
    }

    @Override
    public CodeLoginMode[] getIdentities() {
        return new CodeLoginMode[] {CodeLoginMode.CODE_WECHAT};
    }

    /**
     * @param username appid
     * @param token#getCredentials() 微信授权码
     */
    @Override
    @SneakyThrows
    public OauthUser getOauthUser(String username, UsernamePasswordAuthenticationToken token) {
        if (username.equals(this.wechatProperties.getAppId())) {
            Map<String, Object> params = new HashMap<>();
            params.put(FIELD_APP_ID, wechatProperties.getAppId());
            params.put(FIELD_SECRET, wechatProperties.getSecret());
            params.put(FIELD_CODE, token.getCredentials());
            params.put(FIELD_GRANT_TYPE, "authorization_code");
            Map<String, Object> result = Jackson.parseObject(
                    HttpClient.get(wechatProperties.getTokenUrl(), params), Map.class, String.class, Object.class);
            if (result.containsKey(FIELD_ACCESS_TOKEN)) {
                String accessToken = (String) result.get(FIELD_ACCESS_TOKEN);
                String phoneNumber = getPhoneNumber(accessToken);
                if (StringUtils.isBlank(phoneNumber)) {
                    throw new IllegalArgumentException(MessageUtils.getMessage("wechat.phone.not.found",
                            "failed to get wechat mobile phone number"));
                }
                //微信用户登录以后，根据微信手机号查询用户信息，如果查询不到用户信息时自动注册
                UserSecret userSecret = UserAccessor.getUserSecret(phoneNumber, signSecret.password(UserConstants.SERVICE_ID));
                if (null == userSecret) {
                    String openId = (String) result.get(FIELD_OPENID);
                    WechatPostDto wechatPostDto = getWechatPostDto(accessToken, openId, phoneNumber);
                    long userId = UserAccessor.addWechat(wechatPostDto, signSecret.password(UserConstants.SERVICE_ID));
                    return UserConverter.toOauthUser(userId, wechatPostDto);
                } else {
                    return UserConverter.toOauthUser(userSecret);
                }
            }
        }
        return null;
    }

    @SneakyThrows
    private String getPhoneNumber(String accessToken) {
        Map<String, Object> params = new HashMap<>();
        params.put(FIELD_ACCESS_TOKEN, accessToken);
        Map<String, Object> result = Jackson.parseObject(
                HttpClient.post(wechatProperties.getPhoneUrl(), params), Map.class, String.class, Object.class);
        if (result.containsKey(FIELD_PHONE_INFO)) {
            result = Jackson.parseObject((String) result.get(FIELD_PHONE_INFO), Map.class, String.class, Object.class);
            return (String) result.get(FIELD_PHONE_NUMBER);
        }
        return null;
    }

    @SneakyThrows
    private WechatPostDto getWechatPostDto(String accessToken, String openId, String phoneNumber) {
        WechatPostDto wechatPostDto = new WechatPostDto();
        wechatPostDto.setOpenId(openId);

        Map<String, Object> params = new HashMap<>();
        params.put(FIELD_ACCESS_TOKEN, accessToken);
        params.put(FIELD_OPENID, openId);
        params.put("lang", "zh_CN");
        Map<String, Object> result = Jackson.parseObject(
                HttpClient.get(wechatProperties.getUserInfoUrl(), params), Map.class, String.class, Object.class);
        if (result.containsKey(FIELD_OPENID)) {
            wechatPostDto.setUsername((String) result.get(FIELD_NICKNAME));
            wechatPostDto.setSex(Enumerable.getByCode(Sex.class, result.get(FIELD_SEX)));
            if (result.containsKey(FIELD_HEADIMGURL)) {
                wechatPostDto.setHeadImg((String) result.get(FIELD_HEADIMGURL));
            }
        }
        wechatPostDto.setPhone(phoneNumber);
        if (StringUtils.isBlank(wechatPostDto.getUsername())) {
            wechatPostDto.setUsername(phoneNumber);
        }
        return wechatPostDto;
    }

    @Override
    public boolean checkCode(String agent, String code) {
        return Boolean.TRUE;
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }
}
