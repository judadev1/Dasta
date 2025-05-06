package juda.dev.dastaprueba1.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para manejar las calificaciones de favores.
 * Permite asignar una puntuación del 1 al 5 y un comentario opcional.
 */
@Data
@NoArgsConstructor
public class CalificacionDTO {

    @NotNull(message = "La nota es obligatoria.")
    @Min(value = 1, message = "La nota mínima es 1.")
    @Max(value = 5, message = "La nota máxima es 5.")
    private Integer nota;

    @Size(max = 1000, message = "El comentario es muy largo.")
    private String comentario;
}