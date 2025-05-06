package juda.dev.dastaprueba1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema.
 * Almacena información personal, credenciales de acceso y saldo disponible.
 * Cada persona puede solicitar favores y calificar servicios recibidos.
 * El saldo inicial por defecto es de $3000.00
 */
@Entity
@Table(name = "persona")
@Data
public class Persona {
    @Id
    @Column(length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre1;

    @Column(length = 100)
    private String nombre2;

    @Column(nullable = false, length = 100)
    private String apellido1;

    @Column(length = 100, nullable = true)
    private String apellido2;

    @Column(length = 20, unique = true)
    private String telefono;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo = new BigDecimal("3000.00");

    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Favor> favores;

    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Set<Calificacion> calificacionesDadas;
}