package juda.dev.dastaprueba1.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.transaction.annotation.Transactional;
import juda.dev.dastaprueba1.DTO.RegistroDTO;
import juda.dev.dastaprueba1.entity.Persona;
import juda.dev.dastaprueba1.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonaService implements UserDetailsService {

    // Logger para registro de eventos
    private static final Logger log = LoggerFactory.getLogger(PersonaService.class);
    
    // Repositorio y encoder necesarios
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Carga los detalles del usuario para Spring Security
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Buscando usuario por username: {}", username);
        Persona persona = personaRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado en loadUserByUsername: {}", username);
                    return new UsernameNotFoundException("Usuario o contraseña incorrectos");
                });
        log.debug("Usuario {} encontrado para login.", username);
        return User.builder()
                .username(persona.getUsername())
                .password(persona.getPassword())
                .roles("USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Busca un usuario por su username
     */
    @Transactional(readOnly = true)
    public Persona findByUsername(String username) {
        return personaRepository.findByUsername(username).orElse(null);
    }

    /**
     * Registra un nuevo usuario en el sistema
     */
    @Transactional
    public Persona registrarNuevaPersona(RegistroDTO registroDTO) {
        log.info("Iniciando registro para email: {}", registroDTO.getEmail());
        if (personaRepository.existsById(registroDTO.getEmail())) {
            log.warn("Intento de registro con email duplicado (servicio): {}", registroDTO.getEmail());
            throw new EntityExistsException("El correo electrónico ya está registrado.");
        }

        // Crea nueva persona con datos del registro
        Persona nuevaPersona = new Persona();
        nuevaPersona.setUsername(registroDTO.getEmail());
        nuevaPersona.setNombre1(registroDTO.getNombre1());
        nuevaPersona.setNombre2(registroDTO.getNombre2());
        nuevaPersona.setApellido1(registroDTO.getApellido1());
        nuevaPersona.setApellido2(registroDTO.getApellido2());
        nuevaPersona.setTelefono(registroDTO.getTelefono());
        nuevaPersona.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        try {
            Persona personaGuardada = personaRepository.save(nuevaPersona);
            log.info("Usuario {} registrado y guardado con éxito.", personaGuardada.getUsername());
            return personaGuardada;
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al guardar usuario {}: {}", registroDTO.getEmail(), e.getMessage());
            String causa = determinarCausaConstraint(e);
            throw new IllegalArgumentException(causa, e);
        } catch (Exception e) {
            log.error("Error inesperado al guardar usuario {}: {}", registroDTO.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Error inesperado al guardar el usuario.", e);
        }
    }

    /**
     * Cambia la contraseña de un usuario
     */
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        Persona persona = findByUsernameOrThrow(username);
        if (!passwordEncoder.matches(oldPassword, persona.getPassword())) {
            log.warn("Intento fallido de cambio de contraseña para {}: Contraseña actual incorrecta.", username);
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }
        persona.setPassword(passwordEncoder.encode(newPassword));
        personaRepository.save(persona);
        log.info("Contraseña actualizada para usuario {}", username);
    }

    /**
     * Busca usuario por username o lanza excepción
     */
    private Persona findByUsernameOrThrow(String username) {
        return personaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Determina la causa específica del error de base de datos
     */
    private String determinarCausaConstraint(DataIntegrityViolationException e) {
        Throwable rootCause = e.getRootCause();
        String msg = (rootCause != null) ? rootCause.getMessage().toLowerCase() : e.getMessage().toLowerCase();

        if (msg.contains("duplicate entry")) {
            if (msg.contains("telefono")) return "El número de teléfono ya está registrado.";
            if (msg.contains("primary")) return "El correo electrónico ya está registrado (error BD).";
            return "Violación de restricción única.";
        }
        if (msg.contains("cannot be null")) {
            if (msg.contains("apellido2")) return "El segundo apellido es obligatorio.";
            return "Un campo obligatorio está vacío.";
        }
        if (msg.contains("data truncation")) {
            return "Algún dato es demasiado largo para el campo correspondiente.";
        }
        return "Error de base de datos al guardar.";
    }
}