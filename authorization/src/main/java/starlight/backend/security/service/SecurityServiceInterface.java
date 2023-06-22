package starlight.backend.security.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

public interface SecurityServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(HttpServletRequest request);

    String getJWTToken(UserDetailsImpl authentication, long id);

    SessionInfo registerSponsor(NewUser newUser);

    SessionInfo registerAdmin(NewUser newUser);

    SessionInfo loginInfoSponsor(HttpServletRequest request);

    SessionInfo loginInfoAdmin(HttpServletRequest request);
}
