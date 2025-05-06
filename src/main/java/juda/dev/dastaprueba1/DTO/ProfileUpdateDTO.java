package juda.dev.dastaprueba1.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar datos del perfil de usuario.
 * Gestiona la información personal básica como nombres, 
 * apellidos y teléfono de contacto.
 */
@Data
public class ProfileUpdateDTO {

    @NotBlank(message = "El primer nombre no puede estar vacío.")
    @Size(max = 100)
    private String nombre1;

    @Size(max = 100)
    private String nombre2;

    @NotBlank(message = "El primer apellido no puede estar vacío.")
    @Size(max = 100)
    private String apellido1;

    @Size(max = 100)
    private String apellido2;

    @Size(max = 20)
    private String telefono;
}