package juda.dev.dastaprueba1.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para el registro de nuevos usuarios.
 * Captura y valida la información personal necesaria 
 * para crear una cuenta: nombres, apellidos, email,
 * teléfono y contraseña con su confirmación.
 */
@Data
public class RegistroDTO {

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

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "Debe ser una dirección de correo electrónico válida.")
    @Size(max = 50)
    private String email;

    @Size(max = 20)
    private String telefono;

    @NotBlank(message = "La contraseña не может быть пустой.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @NotBlank(message = "La confirmación de contraseña no puede estar vacía.")
    private String confirmPassword;
}