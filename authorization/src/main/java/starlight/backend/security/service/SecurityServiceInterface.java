package starlight.backend.security.service;

import org.springframework.security.core.Authentication;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

public interface SecurityServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(Authentication auth);

    String getJWTToken(UserDetailsImpl authentication, long id);

    String createScope(UserDetailsImpl authentication);

    SessionInfo registerSponsor(NewUser newUser);

    SessionInfo registerAdmin(NewUser newUser);

    SessionInfo loginInfoSponsor(Authentication auth);

    SessionInfo loginInfoAdmin(Authentication auth);
}
