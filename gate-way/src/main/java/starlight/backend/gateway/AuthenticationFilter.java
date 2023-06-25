package starlight.backend.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!validator.isSecured(exchange.getRequest().getPath().toString())) {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                Jws<Claims> claimsJws = jwtUtil.validateToken(authHeader);
                Claims claims = claimsJws.getBody();

                String role = claims.get("role", String.class);
                if (role == null || (!role.equals("ROLE_ADMIN")
                        && !role.equals("ROLE_TALENT")
                        && !role.equals("ROLE_SPONSOR"))) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid role");
                }

                String status = claims.get("status", String.class);
                if (status == null || !status.equals("ACTIVE")) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid status");
                }

                long issuedAt = claims.getIssuedAt().getTime() / 1000;
                long currentTimestamp = System.currentTimeMillis() / 1000;
                long maxTokenAgeSeconds = 180 * 60;
                if (currentTimestamp - issuedAt > maxTokenAgeSeconds) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
                }

                String id = claims.get("sub", String.class);
                if (((!exchange.getRequest().getPath().toString().equals("/api/v1/talents/" + id))
                        || !role.equals("ROLE_TALENT"))
                        && (exchange.getRequest().getMethod() == HttpMethod.PATCH
                        || exchange.getRequest().getMethod() == HttpMethod.DELETE)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot change profile another talent!!");
                }

            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}