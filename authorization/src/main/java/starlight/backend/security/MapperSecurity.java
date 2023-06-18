package starlight.backend.security;

import org.mapstruct.Mapper;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.model.enums.Role;
import starlight.backend.user.model.enums.SponsorStatus;
import starlight.backend.user.model.response.Talent;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface MapperSecurity {
    default SessionInfo toSessionInfo(String token) {
        return SessionInfo.builder()
                .token(token)
                .build();
    }

    default UserDetailsImpl toUserDetailsImplTalent(Talent talent, UserEntity user) {
        return new UserDetailsImpl(
                talent.email(),
                talent.password(),
                Role.valueOf(user.getRole().getName().substring("ROLE_".length())),
                SponsorStatus.ACTIVE
        );
    }
/*
    default UserDetailsImpl toUserDetailsImplSponsor(UserEntity user) {
        return new UserDetailsImpl(
                user.getSponsor().getEmail(),
                user.getSponsor().getPassword(),
                Role.valueOf(user.getRole().getName().substring("ROLE_".length())),
                user.getSponsor().getStatus()
        );
    }*/

    default UserDetailsImpl toUserDetailsImplAdmin(UserEntity user) {
        return new UserDetailsImpl(
                user.getAdmin().getEmail(),
                user.getAdmin().getPassword(),
                Role.valueOf(user.getRole().getName().substring("ROLE_".length())),
                SponsorStatus.ACTIVE
        );
    }
}
