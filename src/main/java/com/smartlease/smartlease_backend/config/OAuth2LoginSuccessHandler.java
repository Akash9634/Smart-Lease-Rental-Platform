package com.smartlease.smartlease_backend.config;

import com.smartlease.smartlease_backend.model.Role;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtService jwtService){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        //find existing user or create new one
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPassword("");  // No password for OAuth users
                    newUser.setRole(Role.ROLE_TENANT);  // Default role
                    newUser.setOauthProvider("GOOGLE");
                    return userRepository.save(newUser);
                });

        //generate JWT
        String jwt = jwtService.generateToken(user);

        //redirect to frontend with token
        String redirectUrl = "http://localhost:5173/oauth2/callback?token=" + jwt;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
