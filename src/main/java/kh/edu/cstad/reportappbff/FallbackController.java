package kh.edu.cstad.reportappbff;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
@RequiredArgsConstructor
@Slf4j
public class FallbackController {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping("/contact-support")
    public Mono<?> fallback() {
        return Mono.just(Map.of(
                "message", "Please wait or contact to support team!"));
    }


    /**
     * Fallback for account service - tries to get data from Redis cache
     */
    @GetMapping(value = "/account-service/api/v1/accounts")
    public Mono<Object> accountServiceFallback(ServerWebExchange exchange) {

        String path = exchange.getRequest().getPath().value();
        log.debug("path: {}", path);

        String cacheKey = path.replace("/fallback", "");
        log.debug("cacheKey: {}", cacheKey);

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .flatMap(json -> {
                    try {
                        Object data = objectMapper.readValue(json, Object.class);
                        return Mono.just(Map.of("cache", data));
                    } catch (Exception ex) {
                        log.error("Invalid JSON in Redis", ex);
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Invalid JSON data in cache")));
                    }
                })
                .switchIfEmpty(Mono.just(
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Cache not found"))
                ));
    }

}
