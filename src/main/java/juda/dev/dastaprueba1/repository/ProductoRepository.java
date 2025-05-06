package juda.dev.dastaprueba1.repository;

import juda.dev.dastaprueba1.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la gestión del catálogo de productos.
 * Permite obtener los productos disponibles por tienda y realizar 
 * operaciones básicas de mantenimiento del catálogo.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Productos, Integer> {
    List<Productos> findByTiendaIdtienda(Integer idtienda);
}