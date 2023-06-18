package starlight.backend.security.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import starlight.backend.admin.AdminRepository;
import starlight.backend.admin.model.emtity.AdminEntity;
import starlight.backend.exception.user.UserNotFoundException;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.model.enums.Role;
import starlight.backend.user.model.response.Talent;
import starlight.backend.user.repository.RoleRepository;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class SecurityServiceImpl implements SecurityServiceInterface {
    private final JwtEncoder jwtEncoder;
    private MapperSecurity mapperSecurity;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AdminRepository adminRepository;
    private RestTemplate restTemplate;

    @Override
    public SessionInfo loginInfo(Authentication auth) {
        log.info("12");
        log.info("auth {}",auth.getName());
        Talent talent = restTemplate.getForObject(
                "http://TALENT/api/v3/talent",
                Talent.class,
                auth.getName()
        );
        log.info("talent {}",talent);
        var user = userRepository.findByTalentId(talent.talentId());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplTalent(talent, user),
                talent.talentId());
        return mapperSecurity.toSessionInfo(token);
    }

    @Override
    public SessionInfo loginInfoSponsor(Authentication auth) {
        //TODO: find by id sponsor
       /* var user = userRepository.findBySponsor_Email(auth.getName());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplSponsor(user),
                user.getSponsor().getSponsorId());
        return mapperSecurity.toSessionInfo(token);*/
        return null;
    }

    @Override
    public SessionInfo loginInfoAdmin(Authentication auth) {
        if (!userRepository.existsByAdmin_Email(auth.getName())) {
            throw new UserNotFoundException(auth.getName());
        }
        var user = userRepository.findByAdmin_Email(auth.getName());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplAdmin(user),
                user.getAdmin().getAdminId());
        return mapperSecurity.toSessionInfo(token);
    }

    @Override
    public SessionInfo register(NewUser newUser) {
        Talent talent = restTemplate.postForObject(
                "http://TALENT/api/v3/talent",
                newUser,
                Talent.class
        );
        log.info("talent {}",talent);
        var role = roleRepository.findByName(Role.TALENT.getAuthority());
        var user = userRepository.save(UserEntity.builder()
                .talentId(talent.talentId())
                .role(role)
                .build());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplTalent(talent, user),
                talent.talentId());
        return mapperSecurity.toSessionInfo(token);
    }

    @Override
    public SessionInfo registerSponsor(NewUser newUser) {
        //TODO: save sponsor
       /* var role = roleRepository.findByName(Role.SPONSOR.getAuthority());
        var user = userRepository.save(UserEntity.builder()
                .role(role)
                .build());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplSponsor(user),
                user.getSponsor().getSponsorId());
        return mapperSecurity.toSessionInfo(token);*/
        return null;
    }

    @Override
    public SessionInfo registerAdmin(NewUser newUser) {
        var admin = adminRepository.save(AdminEntity.builder()
                .fullName(newUser.fullName())
                .email(newUser.email())
                .password(passwordEncoder.encode(newUser.password()))
                .build());
        var role = roleRepository.findByName(Role.ADMIN.getAuthority());
        var user = userRepository.save(UserEntity.builder()
                .role(role)
                .admin(admin)
                .build());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplAdmin(user),
                user.getAdmin().getAdminId());
        return mapperSecurity.toSessionInfo(token);
    }

    @Override
    @Transactional(readOnly = true)
    public String getJWTToken(UserDetailsImpl authentication, long id) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(180, MINUTES))
                .subject(String.valueOf(id))
                .claim("scope", createScope(authentication))
                .claim("status", authentication.getStatus())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    @Transactional(readOnly = true)
    public String createScope(UserDetailsImpl authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
