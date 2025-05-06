// Servicio que gestiona los productos y su relación con las tiendas
package juda.dev.dastaprueba1.service;

import juda.dev.dastaprueba1.entity.Productos;
import juda.dev.dastaprueba1.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    // Repositorio para acceso a datos de productos
    private final ProductoRepository productoRepository;

    /**
     * Obtiene la lista de productos de una tienda específica
     * @param idTienda ID de la tienda a consultar
     * @return Lista de productos o lista vacía si idTienda es null
     */
    @Transactional(readOnly = true)
    public List<Productos> getProductosPorTienda(Integer idTienda) {
        if (idTienda == null) {
            return List.of();
        }
        return productoRepository.findByTiendaIdtienda(idTienda);
    }
}