package com.accurascience.util;

import com.accurascience.dao.UserDao;
import com.accurascience.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义添加额外参数（jwt）
 */
@Component
public class TokenEnhancerUtil implements TokenEnhancer {
    @Autowired
    private UserDao ud;
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        //获得用户详细信息
        String username = ((org.springframework.security.core.userdetails.User)authentication.getUserAuthentication()
                .getPrincipal()).getUsername();
        //String username = authentication.getUserAuthentication().getPrincipal();//jwt获得用户名的方式
        User user = ud.checkUser(username);
        Map<String, Object> additionalInfo = new HashMap<String, Object>(2);
        //jwt添加额外信息
        additionalInfo.put("id", user.getUserId());
        additionalInfo.put("email", user.geteMail());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}
