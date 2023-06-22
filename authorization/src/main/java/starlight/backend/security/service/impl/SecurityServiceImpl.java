package starlight.backend.security.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import starlight.backend.admin.AdminRepository;
import starlight.backend.admin.model.emtity.AdminEntity;
import starlight.backend.exception.user.UserNotFoundException;
import starlight.backend.security.JwtService;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.model.entity.RoleEntity;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.model.enums.Role;
import starlight.backend.user.model.response.Talent;
import starlight.backend.user.repository.RoleRepository;
import starlight.backend.user.repository.UserRepository;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class SecurityServiceImpl implements SecurityServiceInterface {
    private MapperSecurity mapperSecurity;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AdminRepository adminRepository;
    private RestTemplate restTemplate;
    private JwtService jwtService;

    @Override
    public SessionInfo loginInfo(Authentication auth) {
        //log.info("auth {}", auth);
        // log.info("auth {}", auth.getName());
        Talent talent = restTemplate.getForObject(
                "http://TALENT/api/v3/talent?email=" + auth.getName(),
                Talent.class
        );
        var user = userRepository.findByTalentId(talent.talent_id());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplTalent(talent, user), talent.talent_id());
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
        NewUser user = NewUser.builder()
                .fullName(newUser.fullName())
                .email(newUser.email())
                .password(passwordEncoder.encode(newUser.password()))
                .build();
        Talent talent = restTemplate.postForObject(
                "http://TALENT/api/v3/talent",
                user,
                Talent.class
        );
        log.info("talent {}", talent);
        if (!roleRepository.existsByName(Role.TALENT.getAuthority())) {
            roleRepository.save(RoleEntity.builder()
                    .name(Role.TALENT.getAuthority())
                    .build());
        }
        var role = roleRepository.findByName(Role.TALENT.getAuthority());
        var userEntity = userRepository.save(UserEntity.builder()
                .talentId(talent.talent_id())
                .role(role)
                .build());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplTalent(talent, userEntity),
                talent.talent_id());
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
        if (!roleRepository.existsByName(Role.ADMIN.getAuthority())) {
            roleRepository.save(RoleEntity.builder()
                    .name(Role.ADMIN.getAuthority())
                    .build());
        }
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
        return jwtService.generateToken(authentication, String.valueOf(id));
    }
}
