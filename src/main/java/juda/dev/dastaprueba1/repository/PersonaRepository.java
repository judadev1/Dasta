package juda.dev.dastaprueba1.repository;

import juda.dev.dastaprueba1.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios del sistema.
 * Permite realizar operaciones básicas y búsquedas por nombre 
 * de usuario para la autenticación y acceso al sistema.
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {
    Optional<Persona> findByUsername(String username);
}