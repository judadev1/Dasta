package juda.dev.dastaprueba1.repository;

import juda.dev.dastaprueba1.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de tiendas.
 * Permite realizar operaciones básicas como crear, consultar,
 * actualizar y eliminar tiendas del sistema.
 */
@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {}