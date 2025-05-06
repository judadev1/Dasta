package juda.dev.dastaprueba1.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * Llave compuesta para la entidad FavorProducto.
 * Combina el ID del favor y el ID del producto para 
 * formar una identificación única de cada ítem en un pedido.
 */
@Embeddable
@Data
@EqualsAndHashCode
public class FavorProductoId implements Serializable {
    private static final long serialVersionUID = -2719838666824116936L;
    private Integer idfavor;
    private Integer idproducto;
}