package models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(
        @Size(min = 16, max = 255, message = "Refresh token must be between 36 and 255 characters")
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {
}
