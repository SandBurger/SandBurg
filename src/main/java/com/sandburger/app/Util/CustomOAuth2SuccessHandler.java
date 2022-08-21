package com.sandburger.app.Util;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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

        // 토큰을 리다이렉트 url에 포함
        String targetUrl = UriComponentsBuilder.fromUriString("/main")
                .queryParam("token", jwtUtil.createToken(claims, JwtUtil.ACCESS_TOKEN_VALIDATION_SECOND))
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
