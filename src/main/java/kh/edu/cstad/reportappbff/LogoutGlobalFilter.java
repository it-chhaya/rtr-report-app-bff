package kh.edu.cstad.reportappbff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class LogoutGlobalFilter implements GlobalFilter {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.registration.nextjs.client-id}")
    private String clientId;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // Filter only /logout
        if (!request.getPath().value().equals("/logout")) {
            return chain.filter(exchange);
        }

        return exchange.getSession()
                .flatMap(WebSession::invalidate)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();
                    String keycloakLogoutUrl = issuerUri + "/protocol/openid-connect/logout" +
                            "?client_id=" + clientId +
                            "&post_logout_redirect_uri=" + baseUrl + "/";
                    // Set redirect response
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().setLocation(URI.create(keycloakLogoutUrl));

                }))
                .then();
    }

}
