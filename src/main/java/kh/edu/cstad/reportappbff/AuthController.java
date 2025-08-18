package kh.edu.cstad.reportappbff;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/is-authenticated")
    public ResponseEntity<?> isAuthenticated(Authentication auth) {
        Map<String, Boolean> data = new HashMap<>();
        data.put("isAuthenticated", auth != null);
        return ResponseEntity.ok(data);
    }
}
