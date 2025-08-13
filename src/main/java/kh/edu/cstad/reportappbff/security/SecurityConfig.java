package kh.edu.cstad.reportappbff.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain configureFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange(exchange ->
                exchange
                        .pathMatchers("/dashboard/**").authenticated()
                        .anyExchange().permitAll()

        );

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);

        http.oauth2Login(Customizer.withDefaults());

        return http.build();
    }

}
