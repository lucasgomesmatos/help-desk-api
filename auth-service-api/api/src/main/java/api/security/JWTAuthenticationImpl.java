package api.security;

import api.security.dtos.UserDetailsDto;
import api.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.requests.AuthenticateRequest;
import models.responses.AuthenticateResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Log4j2
@RequiredArgsConstructor
public class JWTAuthenticationImpl {

    private final AuthenticationManager authenticationManager;

    private final JWTUtils jwtUtils;

    public AuthenticateResponse authenticate(final AuthenticateRequest request) {

        try {
            final var authRequest = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            return buildResponse((UserDetailsDto) authRequest.getPrincipal());
        } catch (BadCredentialsException exception) {
            log.error("Invalid credentials: {}", exception.getMessage());
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    protected AuthenticateResponse buildResponse(final UserDetailsDto detailsDto) {

        final var token = jwtUtils.generateToken(detailsDto);

        return AuthenticateResponse.builder()
                .type("Bearer")
                .token(token)
                .build();
    }
}
