package juda.dev.dastaprueba1.config;

import juda.dev.dastaprueba1.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Servicios necesarios para la autenticación
    private final PersonaService personaService;
    private final PasswordEncoder passwordEncoder;

    // Rutas públicas que no requieren autenticación
    private static final String[] RECURSOS_PUBLICOS = {
            "/css/**", "/js/**", "/images/**", "/webjars/**",
            "/login", "/register", "/error"
    };

    /**
     * Configura la cadena de filtros de seguridad
     * Define permisos de acceso y comportamiento de autenticación
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configura permisos de acceso a rutas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(RECURSOS_PUBLICOS).permitAll() // Recursos públicos
                        .requestMatchers("/favores/nuevo/**", "/favores/crear/**", "/favores/calificar/**", "/favores/historial", "/profile/**", "/calificaciones/**").authenticated() // Rutas protegidas
                        .requestMatchers("/").permitAll() // Página principal pública
                        .anyRequest().authenticated() // Resto requiere autenticación
                )
                // Configura el formulario de login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // Configura el proceso de logout
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )
                // Configura remember-me para sesiones persistentes
                .rememberMe(remember -> remember
                        .key("clave-secreta-unica-diferente")
                        .tokenValiditySeconds(86400 * 14) // 14 días
                        .userDetailsService(personaService)
                );

        return http.build();
    }

    /**
     * Configura el proveedor de autenticación con el servicio de usuarios
     * y el codificador de contraseñas
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personaService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}