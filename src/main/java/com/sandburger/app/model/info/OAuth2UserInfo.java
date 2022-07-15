package com.sandburger.app.model.info;

import com.sandburger.app.model.Provider;

public interface OAuth2UserInfo {
    String getOAuthRedirectURL();

    String requestAccessToken(String code);

    default Provider type() {
        if (this instanceof GoogleOAuth2UserInfo) {
            return Provider.GOOGLE;
        } else {
            return null;
        }
    }
}
