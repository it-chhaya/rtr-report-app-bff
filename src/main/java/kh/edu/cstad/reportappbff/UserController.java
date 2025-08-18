package kh.edu.cstad.reportappbff;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public UserProfile me(@AuthenticationPrincipal Authentication auth) {

        OAuth2AuthenticationToken oauth2 = (OAuth2AuthenticationToken) auth;
        OAuth2User oAuth2User = oauth2.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return UserProfile.builder()
                .username(attributes.get("preferred_username").toString())
                .email(attributes.get("email").toString())
                .familyName(attributes.get("family_name").toString())
                .givenName(attributes.get("given_name").toString())
                .build();
    }

}
