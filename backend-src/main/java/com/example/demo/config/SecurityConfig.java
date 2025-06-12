package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.CustomOAuth2UserService;
import com.example.demo.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// CORS is now handled by SimpleCorsFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())  // Disable Spring Security's CORS as we handle it with our filter
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/**"
                ).permitAll())
            .httpBasic(httpBasic -> httpBasic.disable())  // Disable HTTP Basic as we use JWT
            .formLogin(form -> form.disable())  // Disable form login
            .oauth2Login(oauth2 -> {
                oauth2.userInfoEndpoint(userInfo -> 
                    userInfo.oidcUserService(customOAuth2UserService)
                );
                oauth2.successHandler(oAuth2LoginSuccessHandler);
            })
            .logout(logout -> {
                OidcClientInitiatedLogoutSuccessHandler successHandler = 
                    new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
                successHandler.setPostLogoutRedirectUri("{baseUrl}");
                
                logout.logoutSuccessHandler(successHandler)
                      .invalidateHttpSession(true)
                      .clearAuthentication(true)
                      .deleteCookies("JSESSIONID");
            })
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // AuthenticationProvider and PasswordEncoder are now defined in ApplicationConfig
}
