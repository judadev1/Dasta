package juda.dev.dastaprueba1.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para el cambio de contraseña de usuario.
 * Maneja la validación de la contraseña actual y asegura que 
 * la nueva cumpla con los requisitos mínimos de seguridad.
 */
@Data
public class PasswordChangeDTO {
    @NotBlank(message = "Contraseña actual requerida")
    private String oldPassword;

    @NotBlank(message = "Nueva contraseña requerida")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    private String newPassword;

    @NotBlank(message = "Confirmación requerida")
    private String confirmPassword;
}