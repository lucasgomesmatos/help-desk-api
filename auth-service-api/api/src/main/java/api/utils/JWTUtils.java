package api.utils;

import api.security.dtos.UserDetailsDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(final UserDetailsDto detailsDto) {
        return Jwts.builder()
                .claim("id", detailsDto.getId())
                .claim("name", detailsDto.getName())
                .claim("authorities", detailsDto.getAuthorities())
                .subject(detailsDto.getUsername())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }
}
