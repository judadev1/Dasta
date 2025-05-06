package juda.dev.dastaprueba1.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO para solicitudes de favores en tiendas.
 * Gestiona pedidos con múltiples productos, incluyendo la tienda 
 * seleccionada, dirección de entrega y repartidor opcional.
 */
@Data
public class FavorStoreRequestDTO {

    @NotNull
    private Integer idTienda;

    private Integer idDelivery;

    @NotBlank
    @Size(max = 255)
    private String direccion;

    @Size(max = 1000)
    private String descripcion;

    @Valid
    @NotEmpty
    private List<FavorProductoItemDTO> items = new ArrayList<>();
}