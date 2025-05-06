package juda.dev.dastaprueba1.controller;

import jakarta.persistence.EntityNotFoundException;
import juda.dev.dastaprueba1.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    // Logger para seguimiento de eventos
    private static final Logger log = LoggerFactory.getLogger(CalificacionController.class);
    
    // Servicio para procesar calificaciones
    private final CalificacionService calificacionService;

    /**
     * Procesa una nueva calificación para un favor
     * @param idfavor ID del favor a calificar
     * @param nota Puntuación (1-5)
     * @param comentario Comentario opcional
     */
    @PostMapping("/rate")
    public String submitRating(
            @RequestParam("idfavor") Integer idfavor,
            @RequestParam("nota") Integer nota,
            @RequestParam(value = "comentario", required = false) String comentario,
            RedirectAttributes redirectAttributes,
            Authentication authentication
    ) {
        // Verifica autenticación del usuario
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Debes iniciar sesión para calificar.");
            return "redirect:/login";
        }
        String currentUsername = authentication.getName();
        log.info("Recibida calificación para favor #{} por usuario {}", idfavor, currentUsername);

        // Valida la nota
        if (nota == null || nota < 1 || nota > 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "La nota proporcionada es inválida (debe ser entre 1 y 5).");
            return "redirect:/favores/historial";
        }

        try {
            // Guarda la calificación
            calificacionService.guardarCalificacion(currentUsername, idfavor, nota, comentario);
            redirectAttributes.addFlashAttribute("successMessage", "¡Calificación guardada y favor marcado como entregado!");
            return "redirect:/favores/historial";

        } catch (IllegalStateException | EntityNotFoundException e) {
            // Manejo de errores de negocio
            log.warn("Error de negocio al intentar calificar favor #{} por {}: {}", idfavor, currentUsername, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo guardar la calificación: " + e.getMessage());
            return "redirect:/favores/historial";
        } catch (Exception e) {
            // Manejo de errores inesperados
            log.error("Error inesperado al guardar calificación para favor #{} por {}: ", idfavor, currentUsername, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al procesar la calificación.");
            return "redirect:/favores/historial";
        }
    }
}