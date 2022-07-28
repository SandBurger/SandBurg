package com.sandburger.app.model;

import com.sandburger.app.Entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String refresh_key;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String refresh_key) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.refresh_key = refresh_key;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .refresh_key("(String)")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .refresh_key(this.refresh_key)
                .build();
    }
}
