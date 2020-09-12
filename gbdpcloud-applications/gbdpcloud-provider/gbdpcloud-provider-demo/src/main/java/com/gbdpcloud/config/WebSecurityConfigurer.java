package com.gbdpcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @version 1.0
 * @ClassName WebSecurityConfigurer
 * @Description //TODO
 * @Author lhf
 * @Date 2019-12-26 18:03
 **/
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redis = new RedisTokenStore(connectionFactory);
        //redis.setPrefix("user-token");
        return redis;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置不需要登录验证
        http. authorizeRequests()
                .antMatchers( "/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .requestMatchers().antMatchers(HttpMethod.POST,"/*")
                .requestMatchers().antMatchers(HttpMethod.GET,"/*")
                .requestMatchers().antMatchers(HttpMethod.OPTIONS,"/*")
                .requestMatchers().antMatchers(HttpMethod.DELETE,"/*")
                .requestMatchers().antMatchers(HttpMethod.PUT,"/*")
                .and()
                .cors()
                .and()
                .csrf().disable();
    }
}
