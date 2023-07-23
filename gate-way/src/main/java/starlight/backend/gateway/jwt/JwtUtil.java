package starlight.backend.gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.gateway.enums.Role;
import starlight.backend.gateway.enums.SponsorStatus;

import java.security.Key;

@Component
public class JwtUtil {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${jwt.secret}")
    private String secret;
    public final String STATUS = "status";
    public final String ROLE = "role";

    public Jws<Claims> validateToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getRole(Claims claims) {
        String role = claims.get(ROLE, String.class);
        if (role == null || (!role.equals(Role.ADMIN.getAuthority())
                && !role.equals(Role.TALENT.getAuthority())
                && !role.equals(Role.SPONSOR.getAuthority()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid role");
        }
        return role;
    }

    public String getStatus(Claims claims) {
        String status = claims.get(STATUS, String.class);
        if (status == null || !status.equals(SponsorStatus.ACTIVE.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid status");
        }
        return status;
    }

    public boolean checkTimeToken(Claims claims) {
        long issuedAt = claims.getIssuedAt().getTime() / 1000;
        long currentTimestamp = System.currentTimeMillis() / 1000;
        long maxTokenAgeSeconds = 180 * 60;
        return currentTimestamp - issuedAt > maxTokenAgeSeconds;
    }

    public boolean checkStatus(String status) {
        return status.equals(SponsorStatus.ACTIVE.name());
    }

    public boolean checkIdWithRole(long id, String role) {
        if (role.equals(Role.TALENT)) {
            return restTemplate.getForObject(
                    "http://TALENT/api/v3/talent/" + id,
                    Boolean.class
            );
        }
        if (role.equals(Role.ADMIN)) {
            return restTemplate.getForObject(
                    "http://AUTH/api/v3/admin/" + id,
                    Boolean.class
            );
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Id : " + id + " do not have this role : " + role);
    }
}