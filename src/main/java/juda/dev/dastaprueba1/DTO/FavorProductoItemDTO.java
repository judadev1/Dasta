package juda.dev.dastaprueba1.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un producto y su cantidad en un pedido.
 * Usado para manejar los items seleccionados en los favores.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavorProductoItemDTO {
    @NotNull
    private Integer idProducto;
    @Min(1)
    private int cantidad = 0;
}