package com.accurascience.config;

import com.accurascience.util.TokenEnhancerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * 认证服务器配置
 * @author zhuchaojie
 * @since 1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	 @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Value("${redirect.uris}")
	private String redirectUris;
//    @Autowired
//    private TokenEnhancerUtil tokenEnhancerUtil;
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	//System.out.println(redirectUris);
        clients.inMemory()
                .withClient("whole-exome")
                .secret("{bcrypt}"/*声明加密格式*/+new BCryptPasswordEncoder().encode("accura-science-2020.03.31"))
                .autoApprove(true) //自动授权配置
                .authorizedGrantTypes("authorization_code","password")//"authorization_code", "refresh_token"
                .scopes("all").redirectUris(redirectUris);
        
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //将自定义 token 添加到增强链中
//        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancerUtil, jwtAccessTokenConverter()));

        endpoints//.tokenStore(jwtTokenStore()).tokenEnhancer(enhancerChain)//jwt支持
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    /**
//     * 对称加密方式
//     * @return
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter(){
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("test");
//        return converter;
//    }
}
