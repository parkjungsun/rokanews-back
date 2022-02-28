package com.pjsun.MilCoevo.domain.user.dto;

import com.pjsun.MilCoevo.domain.user.ProviderType;
import com.pjsun.MilCoevo.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private ProviderType providerType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture,
                           ProviderType providerType) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.providerType = providerType;
    }

    public static OAuthAttributes of(ProviderType providerType,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        switch (providerType) {
            case NAVER:
                return ofNaver("id", attributes);
            case KAKAO:
                return ofKakao(userNameAttributeName, attributes);
            default:
                return ofGoogle(userNameAttributeName, attributes);
        }
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName,
                                          Map<String, Object> attributes) {

        Map<String, Object> kakao_account =
                (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile
                = (Map<String, Object>) kakao_account.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .providerType(ProviderType.KAKAO)
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName,
                                          Map<String, Object> attributes) {

        Map<String, Object> response
                = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .providerType(ProviderType.NAVER)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .providerType(ProviderType.GOOGLE)
                .build();
    }

    public User toEntity() {
        return User.createByOAuthBuilder()
                .email(email)
                .password(UUID.randomUUID().toString())
                .providerType(providerType)
                .build();
    }
}
