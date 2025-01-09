package models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @Schema(description = "Requester ID", example = "67700cefc29dfa75acd0ff86")
        @NotBlank(message = "The Requester ID is required")
        @Size(min = 24, max = 36, message = "The Requester ID must be between 24 and 36 characters")
        String requesterId,

        @Schema(description = "Customer ID", example = "67700cefc29dfa75acd0ff86")
        @NotBlank(message = "The Customer ID is required")
        @Size(min = 24, max = 36, message = "The Customer ID must be between 24 and 36 characters")
        String customerId,

        @Schema(description = "Title", example = "My printer is not working")
        @NotBlank(message = "The Title is required")
        @Size(min = 3, max = 50, message = "The Title must be between 3 and 50 characters")
        String title,

        @Schema(description = "Description", example = "The printer is not printing the documents")
        @NotBlank(message = "The Description is required")
        @Size(min = 10, max = 3000, message = "The Description must be between 10 and 3000 characters")
        String description,

        @Schema(description = "Status", example = "Open")
        @NotBlank(message = "The Status is required")
        @Size(min = 4, max = 15, message = "The Status must be between 4 and 15 characters")
        String status
) {
}
