package juda.dev.dastaprueba1.repository;

import juda.dev.dastaprueba1.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar las calificaciones de favores.
 * Proporciona métodos para verificar la existencia de calificaciones 
 * y realizar operaciones CRUD básicas.
 */
@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    boolean existsByFavorIdfavor(Integer idfavor);
}