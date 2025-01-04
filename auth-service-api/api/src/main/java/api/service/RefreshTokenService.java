package api.service;

import api.models.RefreshToken;
import api.repositories.RefreshTokenRepository;
import api.security.dtos.UserDetailsDto;
import api.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import models.exceptions.RefreshTokenExpiredException;
import models.exceptions.ResourceNotFoundException;
import models.responses.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserDetailsService userDetailsService;
    private final JWTUtils jwtUtils;

    @Value("${jwt.expiration-sec.refresh-token}")
    private Long expiration;

    public RefreshToken save(final String username) {

        final var refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusSeconds(expiration))
                .createdAt(LocalDateTime.now())
                .username(username)
                .build();

        return repository.save(refreshToken);
    }

    public RefreshTokenResponse refreshToken(final String refreshTokenId) {

        final var refreshToken = repository.findById(refreshTokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found: Id:" + refreshTokenId));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            repository.delete(refreshToken);
            throw new RefreshTokenExpiredException("Refresh token expired: Id:" + refreshTokenId);
        }

        final var user = userDetailsService.loadUserByUsername(refreshToken.getUsername());

        final var token = jwtUtils.generateToken((UserDetailsDto) user);

        return RefreshTokenResponse.builder()
                .refreshToken(token)
                .build();
    }
}
