package models.requests;

public record OrderResponse(
        Long id,
        String requesterId,
        String customerId,
        String title,
        String description,
        String status,
        String createdAt,
        String closedAt
) {
}
