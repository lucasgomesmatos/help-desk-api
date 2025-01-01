package br.com.helpdesk.commons.models.requests;

import br.com.helpdesk.commons.models.enums.ProfileEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.With;

import java.util.Set;

@With
public record CreateUserRequest(

        @Schema(description = "User name", example = "John Doe")
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters")
        String name,

        @Schema(description = "User email", example = "johndoe@gmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Size(min = 5, max = 50, message = "Email must contain between 5 and 50 characters")
        String email,

        @Schema(description = "User password", example = "123456")
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 50, message = "Password must contain between 6 and 50 characters")
        String password,

        @Schema(description = "User profiles", example = "[\"ROLE_ADMIN\", \"ROLE_CUSTOMER\"]")
        Set<ProfileEnum> profiles

) {
}
