package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa la relación entre un Favor y los Productos solicitados.
 * Funciona como una tabla intermedia que almacena la cantidad de cada 
 * producto en un favor específico. Utiliza una llave compuesta para 
 * garantizar la unicidad de cada combinación favor-producto.
 */
@Entity
@Table(name = "favor_producto")
@Data
public class FavorProducto {
    @EmbeddedId
    private FavorProductoId id = new FavorProductoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idfavor") // Mapea idfavor del EmbeddedId
    @JoinColumn(name = "idfavor", insertable=false, updatable=false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Favor favor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idproducto") // Mapea idproducto del EmbeddedId
    @JoinColumn(name = "idproducto", insertable=false, updatable=false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Productos producto;

    @Column(nullable = false)
    private int cantidad;

    public void setFavor(Favor favor) {
        this.favor = favor;
        this.id.setIdfavor(favor.getIdfavor());
    }
    public void setProducto(Productos producto) {
        this.producto = producto;
        this.id.setIdproducto(producto.getIdproductos());
    }
}