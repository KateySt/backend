package starlight.backend.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@AllArgsConstructor
@Slf4j
class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> c
                .requestMatchers(antMatcher("/h2/**")).permitAll()
                /////////////////////////OpenApi///////////////////////////////////////////////////
                .requestMatchers(antMatcher("/api-docs/**")).permitAll()
                .requestMatchers(antMatcher("/swagger-resources/**")).permitAll()
                .requestMatchers(antMatcher("/configuration/**")).permitAll()
                .requestMatchers(antMatcher("/swagger*/**")).permitAll()
                .requestMatchers(antMatcher("/webjars/**")).permitAll()
                /////////////////////////DevOps////////////////////////////////////////////////////
                .requestMatchers("/error").permitAll()
                /////////////////////////Email/////////////////////////////////////////////////////
                .requestMatchers("/api/v1/sponsors/forgot-password").permitAll()
                .requestMatchers("/api/v1/sponsors/recovery-password").permitAll()
                /////////////////////////Actuator//////////////////////////////////////////////////
                .requestMatchers(antMatcher("/actuator/**")).permitAll()
                /////////////////////////Production////////////////////////////////////////////////
                .requestMatchers("/api/v1/skills").permitAll()
                .requestMatchers("/api/v1/talents").permitAll()
                .requestMatchers("/api/v1/sponsors").permitAll()
                .requestMatchers("/api/v1/admin").permitAll()
                .requestMatchers("/api/v2/talents").permitAll()
                .requestMatchers("/api/v1/proofs").permitAll()
                .requestMatchers("/api/v2/proofs").permitAll()
                .requestMatchers(POST, "/api/v1/talents/login").permitAll()
                .requestMatchers(POST, "/api/v1/sponsors/login").permitAll()
                .requestMatchers(POST, "/api/v1/admin/login").permitAll()
                .requestMatchers(antMatcher("/api/v1/proofs/**")).permitAll()
                .requestMatchers("/api/v1/sponsors/recovery-account").permitAll()
                /////////////////////////Another///////////////////////////////////////////////////
                .anyRequest().authenticated()
        );
        http.csrf().disable();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
 /*   @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

*/
}