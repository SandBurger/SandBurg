package com.sandburger.app.Util;

import com.sandburger.app.Entity.UserEntity;
import com.sandburger.app.Repository.UserRepository;
import com.sandburger.app.model.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sandburger.app.Util.JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    public final static String REFRESH_TOKEN = "refresh_token";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        log.info("OAuth2USer = {}", oAuth2User);
        // 최초 로그인 시 회원가입 처리(DB에 등록)
        Token token = jwtUtil.generateJWT(email, "USER");
        log.info("access token = {}, refresh token = {}", token.getAccessToken(), token.getRefreshToken());

        Claims claims = Jwts.claims();
        claims.put("tokens", token);

        String targetUrl = UriComponentsBuilder.fromUriString("/main")
                .queryParam("token", token.getAccessToken())
                .build().toUriString();

        userRepository.save(
                UserEntity.builder()
                        .email(email)
                        .build()
        );

        redisUtil.setDataExpire(token.getRefreshToken(), email, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);

        int cookieMaxAge = (int) REFRESH_TOKEN_VALIDATION_SECOND / 60;

        deleteCookie(request, response, REFRESH_TOKEN);
        addCookie(response, REFRESH_TOKEN, token.getRefreshToken(), cookieMaxAge);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }
}
