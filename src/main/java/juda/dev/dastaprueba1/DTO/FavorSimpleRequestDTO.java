package juda.dev.dastaprueba1.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO para crear solicitudes de favores simples.
 * Contiene la información básica necesaria: descripción, dirección, 
 * valor a pagar y referencias opcionales a tienda y repartidor.
 */
@Data 
public class FavorSimpleRequestDTO {

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 1000)
    private String descripcion;

    @NotBlank(message = "La dirección no puede estar vacía.") 
    @Size(max = 255)
    private String direccion;

    @NotNull(message = "Debe indicar un valor.")
    @DecimalMin(value = "0.01", message = "El valor debe ser positivo.")
    @Digits(integer=8, fraction=2)
    private BigDecimal valor;

    private Integer idTienda;
    private Integer idDelivery;
}