package starlight.backend.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/api-docs/**",
            "/swagger-resources/**",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**",
            "/eureka",

            "/error",

            "/api/v1/sponsors/forgot-password",
            "/api/v1/sponsors/recovery-password",
            "/actuator/**",

            "/api/v1/skills",
            "/api/v1/talents",
            "/api/v1/sponsors",
            "/api/v1/admin",
            "/api/v2/talents",
            "/api/v1/proofs",
            "/api/v2/proofs",
            "/api/v1/talents/login",
            "/api/v1/sponsors/login",
            "/api/v1/admin/login",
            "/api/v1/proofs/**",
            "/api/v1/sponsors/recovery-account"
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}