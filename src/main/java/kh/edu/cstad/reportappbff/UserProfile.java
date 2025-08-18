package kh.edu.cstad.reportappbff;

import lombok.Builder;

@Builder
public record UserProfile(
        String username,
        String email,
        String familyName,
        String givenName
) {
}
