package com.melody.todoquadrantappback.security;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

// 這個服務會在 OAuth2 登入時被呼叫，並處理使用者資料
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // 取得 Google 回傳的使用者資料
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 你可以在這裡存進資料庫或建立使用者帳號
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        System.out.println("登入者名稱：" + name);
        System.out.println("登入者信箱：" + email);

        return oauth2User;
    }
}
