package starlight.backend.security.service;

import starlight.backend.security.model.UserDetailsImpl;

public interface JwtServiceInterface {
    void validateToken(final String token);

    String generateToken(UserDetailsImpl authentication, String userId);
}
