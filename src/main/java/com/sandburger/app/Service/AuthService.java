package com.sandburger.app.Service;

import com.sandburger.app.model.Provider;
import com.sandburger.app.model.info.GoogleOAuth2UserInfo;
import com.sandburger.app.model.info.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final List<OAuth2UserInfo> oAuth2UserInfos;
    private final HttpServletResponse response;

    public void request(Provider provider) {
        OAuth2UserInfo oAuth2UserInfo = this.findProviderByType(provider);
        String redirectURL = oAuth2UserInfo.getOAuthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String requestAccessToken(Provider provider, String code) {
        OAuth2UserInfo oAuth2UserInfo = this.findProviderByType(provider);
        return oAuth2UserInfo.requestAccessToken(code);
    }

    private OAuth2UserInfo findProviderByType(Provider provider) {
        return oAuth2UserInfos.stream()
                .filter(x -> x.type() == provider)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }
}
