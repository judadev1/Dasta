package juda.dev.dastaprueba1.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import juda.dev.dastaprueba1.entity.Calificacion;
import juda.dev.dastaprueba1.entity.EstadoFavor;
import juda.dev.dastaprueba1.entity.Favor;
import juda.dev.dastaprueba1.repository.CalificacionRepository;
import juda.dev.dastaprueba1.repository.FavorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    // Logger para el registro de eventos
    private static final Logger log = LoggerFactory.getLogger(CalificacionService.class);
    
    // Repositorios y servicios necesarios
    private final CalificacionRepository calificacionRepository;
    private final FavorRepository favorRepository;
    private final FavorService favorService;

    /**
     * Guarda una nueva calificación para un favor
     * @param username Usuario que califica
     * @param idFavor ID del favor a calificar
     * @param nota Puntuación (1-5)
     * @param comentario Comentario opcional
     */
    @Transactional
    public void guardarCalificacion(String username, Integer idFavor, Integer nota, String comentario) {
        log.info("Guardando calificación para favor #{} por usuario {}", idFavor, username);
        
        // Valida rango de la nota
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("La nota debe estar entre 1 y 5.");
        }

        // Obtiene y valida el favor
        Favor favor = obtenerYValidarFavorParaCalificar(username, idFavor);

        // Crea nueva calificación
        Calificacion calificacion = new Calificacion();
        calificacion.setFavor(favor);
        calificacion.setDelivery(favor.getDelivery());
        calificacion.setPersona(favor.getPersona());
        calificacion.setNota(nota.byteValue());
        calificacion.setComentario(comentario);

        try {
            // Guarda la calificación
            calificacionRepository.save(calificacion);
            log.info("Calificación guardada para favor #{}", idFavor);

            // Actualiza estado del favor
            favorService.actualizarEstadoFavor(idFavor, EstadoFavor.ENTREGADO);
            log.info("Favor #{} marcado como ENTREGADO después de calificar.", idFavor);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de BD al guardar calificación para favor #{}: {}", idFavor, e.getMessage());
            throw new RuntimeException("Error de base de datos al guardar la calificación.", e);
        } catch (Exception e) {
            log.error("Error inesperado al guardar calificación para favor #{}: {}", idFavor, e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar la calificación.", e);
        }
    }

    /**
     * Valida que un favor pueda ser calificado
     * Verifica existencia, propiedad, asignación de delivery y calificación previa
     */
    private Favor obtenerYValidarFavorParaCalificar(String username, Integer idFavor) {
        log.debug("Validando favor #{} para calificar por {}", idFavor, username);
        
        // Busca el favor
        Favor favor = favorRepository.findById(idFavor)
                .orElseThrow(() -> new EntityNotFoundException("Favor no encontrado con ID: " + idFavor));

        // Valida que el usuario sea el propietario
        if (!favor.getPersona().getUsername().equals(username)) {
            log.warn("Usuario {} intentó calificar favor #{} que no le pertenece.", username, idFavor);
            throw new IllegalStateException("No puedes calificar un favor que no solicitaste.");
        }

        // Valida asignación de delivery
        if (favor.getDelivery() == null) {
            log.warn("Intento de calificar favor #{} sin delivery asignado.", idFavor);
            throw new IllegalStateException("Este favor no tiene un repartidor asignado.");
        }

        // Valida que no exista calificación previa
        if (calificacionRepository.existsByFavorIdfavor(favor.getIdfavor())) {
            log.warn("Intento de calificar favor #{} que ya tiene calificación.", idFavor);
            throw new IllegalStateException("Este favor ya ha sido calificado.");
        }
        
        log.debug("Validación OK para calificar favor #{}", idFavor);
        return favor;
    }
}