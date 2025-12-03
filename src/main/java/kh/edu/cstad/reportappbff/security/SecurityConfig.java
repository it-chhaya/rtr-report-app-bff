package kh.edu.cstad.reportappbff.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain configureFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange(exchange ->
                exchange
                        .pathMatchers("/logout").authenticated()
                        .pathMatchers("/dashboard/**").authenticated()
                        .pathMatchers("/api/v1/users/**").authenticated()
                        .anyExchange().permitAll()

        );

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);
        http.logout(ServerHttpSecurity.LogoutSpec::disable);

        http.oauth2Login(Customizer.withDefaults());

//        http.logout(logoutSpec ->
//                logoutSpec.logoutUrl("/logout")
//                        .logoutSuccessHandler(logoutSuccessHandler())
//        );

        return http.build();
    }

//    ServerLogoutSuccessHandler logoutSuccessHandler() {
//        RedirectServerLogoutSuccessHandler redirectServerLogoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
//
//        String keycloakLogoutUrl = issuerUri + "/protocol/openid-connect/logout" +
//                "?client_id=" + clientId +
//                "&post_logout_redirect_uri=http://localhost:8000/";
//
//
//        URI logoutSuccessUrl = URI.create(keycloakLogoutUrl);
//        redirectServerLogoutSuccessHandler.setLogoutSuccessUrl(logoutSuccessUrl);
//
//        return redirectServerLogoutSuccessHandler;
//    }

}
