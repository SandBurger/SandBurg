package com.sandburger.app.Util;

import com.sandburger.app.Entity.UserEntity;
import com.sandburger.app.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sandburger.app.Util.JwtUtil.AUTHORIZATION_HEADER;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String refreshKey = jwtUtil.createRefreshKey(oAuth2User);
        UserEntity user = UserEntity.builder()
                .refresh_key(refreshKey)
                .build();
        userRepository.save(user);
        response.addHeader(AUTHORIZATION_HEADER, "Bearer " + refreshKey);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
