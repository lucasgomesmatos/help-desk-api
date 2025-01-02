package api.controllers.impl;

import api.controllers.AuthController;
import api.security.dtos.JWTAuthenticationImpl;
import api.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import models.requests.AuthenticateRequest;
import models.responses.AuthenticateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {


    private final JWTUtils jwtUtils;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Override
    public ResponseEntity<AuthenticateResponse> authenticate(AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(
                new JWTAuthenticationImpl(authenticationConfiguration.getAuthenticationManager(), jwtUtils).authenticate(request)
        );
    }

    @Override
    public ResponseEntity<AuthenticateResponse> refresh() {
        return null;
    }
}
