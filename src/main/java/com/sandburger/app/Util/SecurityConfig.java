package com.sandburger.app.Util;


import com.sandburger.app.Repository.UserRepository;
import com.sandburger.app.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class);
        http.authorizeRequests()
                    .antMatchers("/oauth2login", "/").permitAll()
                    .anyRequest().authenticated();
        http.oauth2Login()
                .loginPage("/oauth2login")
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(configSuccessHandler());
    }

    private CustomOAuth2SuccessHandler configSuccessHandler() {
        return new CustomOAuth2SuccessHandler(userRepository, jwtUtil);
    }
}
