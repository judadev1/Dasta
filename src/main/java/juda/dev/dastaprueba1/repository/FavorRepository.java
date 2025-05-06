package juda.dev.dastaprueba1.repository;

import juda.dev.dastaprueba1.entity.Favor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de favores (solicitudes de servicio).
 * Permite buscar favores por usuario y combinar criterios de búsqueda 
 * dinámicos mediante especificaciones JPA.
 */
@Repository
public interface FavorRepository extends JpaRepository<Favor, Integer>, JpaSpecificationExecutor<Favor> {
    List<Favor> findByPersonaUsernameOrderByFechaCreacionDesc(String username);
    Optional<Favor> findByIdfavorAndPersonaUsername(Integer idfavor, String username);
}