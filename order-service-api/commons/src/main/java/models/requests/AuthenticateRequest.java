package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record AuthenticateRequest(

        @Schema(description = "User email", example = "johndoe@gmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Size(min = 5, max = 50, message = "Email must contain between 5 and 50 characters")
        String email,

        @Schema(description = "User password", example = "123456")
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 50, message = "Password must contain between 6 and 50 characters")
        String password
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}