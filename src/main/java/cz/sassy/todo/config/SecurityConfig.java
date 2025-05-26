package cz.sassy.todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig is a configuration class that sets up Spring Security for the application.
 * It configures authentication, authorization, and password encoding.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * UserDetailsService bean that provides user details for authentication.
     * This service is used by the authentication provider to load user-specific data.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * SecurityFilterChain bean that configures HTTP security for the application.
     * It sets up CSRF protection, request authorization, form login, and logout handling.
     *
     * @param httpSecurity the HttpSecurity object to configure security settings.
     * @return a SecurityFilterChain instance configured with the specified security settings.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers("/", "/update", "/{id}/update", "/{id}/toggle", "/{id}/delete", "/register/**").permitAll()
                            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                            .requestMatchers("/private/**").hasAnyRole("ADMIN", "USER")
                            .anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/login")
                            .successHandler(new MyAuthenticationSuccessHandler())
                            .permitAll();
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                )

                .build();
    }

    /**
     * UserDetailsService bean that provides user details for authentication.
     * This service is used by the authentication provider to load user-specific data.
     *
     * @return a UserDetailsService instance configured with the application's user details.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    /**
     * AuthenticationProvider bean that uses DaoAuthenticationProvider to authenticate users.
     * It uses the UserDetailsService and PasswordEncoder beans for user details and password encoding.
     *
     * @return an AuthenticationProvider instance configured with UserDetailsService and PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * PasswordEncoder bean that uses BCrypt for encoding passwords.
     * This is used to securely hash passwords before storing them in the database.
     *
     * @return a PasswordEncoder instance configured with BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}