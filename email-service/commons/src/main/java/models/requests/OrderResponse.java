package models.requests;

import java.io.Serial;
import java.io.Serializable;

public record OrderResponse(
        Long id,
        String requesterId,
        String customerId,
        String title,
        String description,
        String status,
        String createdAt,
        String closedAt
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
