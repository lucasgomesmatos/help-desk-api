package models.responses;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
        String refreshToken
) {
}
