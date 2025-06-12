package com.example.demo.security;

import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final UserService userService;

    @Autowired
    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        String email = oauthUser.getEmail();
        String name = oauthUser.getFullName();

        // Process the OAuth2 user (create if not exists)
        userService.processOAuthPostLogin(email, name);

        // Build the redirect URL with query parameters
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/login/success")
                .queryParam("email", email)
                .queryParam("name", name)
                .build().toUriString();

        // Redirect to the frontend with the success parameters
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
