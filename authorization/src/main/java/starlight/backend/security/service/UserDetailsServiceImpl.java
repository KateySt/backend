package starlight.backend.security.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import starlight.backend.security.MapperSecurity;
import starlight.backend.user.model.response.Talent;
import starlight.backend.user.repository.UserRepository;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private MapperSecurity mapper;
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Talent talent = restTemplate.getForObject(
                "http://TALENT/api/v3/talent?email=" + email,
                Talent.class
        );
        if (userRepository.existsByAdmin_Email(email)) {
            return mapper.toUserDetailsImplAdmin(userRepository.findByAdmin_Email(email));
        } else if (talent.email() != null) {
            var user = userRepository.findByTalentId(talent.talent_id());
            return mapper.toUserDetailsImplTalent(talent, user);
        } else {
            var user = userRepository.findByTalentId(talent.talent_id());
            return mapper.toUserDetailsImplTalent(talent, user);
            //TODO: sponsor
            // return mapper.toUserDetailsImplSponsor(userRepository.findBySponsor_Email(email));
        }
    }
}
