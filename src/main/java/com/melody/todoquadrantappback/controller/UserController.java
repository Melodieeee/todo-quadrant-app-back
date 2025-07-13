package com.melody.todoquadrantappback.controller;

import com.melody.todoquadrantappback.dto.UserInfoResponse;
import com.melody.todoquadrantappback.exception.UserUnauthorizedException;
import com.melody.todoquadrantappback.model.User;
import com.melody.todoquadrantappback.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public UserInfoResponse getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {

        User user = userService.getAuthenticatedUser(oAuth2User);
        return new UserInfoResponse(user.getName(), user.getEmail(), user.getPicture());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // 清除 session
        request.getSession().invalidate();

        // 呼叫 servlet 層的 logout（如果你有使用 @AuthenticationPrincipal 等）
        request.logout();

        response.setStatus(HttpServletResponse.SC_OK);

        System.out.println("登出");
    }
}
