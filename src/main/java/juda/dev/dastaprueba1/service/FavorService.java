// Servicio central que maneja toda la lógica de negocio relacionada con favores
package juda.dev.dastaprueba1.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import juda.dev.dastaprueba1.DTO.*;
import juda.dev.dastaprueba1.entity.*;
import juda.dev.dastaprueba1.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavorService {

    // Logger para registro de eventos
    private static final Logger log = LoggerFactory.getLogger(FavorService.class);

    // Repositorios necesarios 
    private final FavorRepository favorRepository;
    private final PersonaRepository personaRepository;
    private final TiendaRepository tiendaRepository;
    private final ProductoRepository productoRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * Crea un favor simple sin productos específicos
     */
    @Transactional
    public Favor crearFavorSimple(String username, FavorSimpleRequestDTO dto) {
        log.info("Procesando crearFavorSimple para usuario: {}", username);
        Persona persona = obtenerPersona(username);
        validarSaldo(persona, dto.getValor());

        // Construye el favor básico
        Favor favor = new Favor();
        favor.setPersona(persona);
        favor.setDescripcion(dto.getDescripcion());
        favor.setDireccion(dto.getDireccion());
        favor.setValor(dto.getValor());
        favor.setEstado(EstadoFavor.SOLICITADO);
        favor.setItemsProducto(new HashSet<>());

        // Asigna tienda si existe
        if (dto.getIdTienda() != null) {
            tiendaRepository.findById(dto.getIdTienda()).ifPresent(favor::setTienda);
        }
        asignarDeliveryOpcional(favor, dto.getIdDelivery());

        return guardarFavor(favor, username, "simple");
    }

    /**
     * Crea un favor con productos de una tienda específica
     */
    @Transactional
    public Favor crearFavorDesdeTienda(String username, FavorStoreRequestDTO dto) {
        log.info("Procesando crearFavorDesdeTienda para usuario: {}", username);
        Persona persona = obtenerPersona(username);
        Tienda tienda = obtenerTienda(dto.getIdTienda());

        // Construye el favor
        Favor favor = new Favor();
        favor.setPersona(persona);
        favor.setTienda(tienda);
        favor.setDireccion(dto.getDireccion());
        favor.setDescripcion(dto.getDescripcion());
        favor.setEstado(EstadoFavor.SOLICITADO);
        
        asignarDeliveryOpcional(favor, dto.getIdDelivery());
        Set<FavorProducto> itemsFavor = procesarItems(dto.getItems(), favor);
        favor.setItemsProducto(itemsFavor);
        
        validarSaldo(persona, favor.getValor());
        return guardarFavor(favor, username, "tienda");
    }

    /**
     * Obtiene todos los favores de un usuario ordenados por fecha
     */
    @Transactional(readOnly = true)
    public List<Favor> getFavoresPorUsuario(String username) {
        log.debug("Buscando favores para usuario: {}", username);
        return favorRepository.findByPersonaUsernameOrderByFechaCreacionDesc(username);
    }

    /**
     * Obtiene historial paginado de favores con filtros
     */
    @Transactional(readOnly = true)
    public Page<Favor> getFavoresHistorialPaginado(String username, String statusFilter, 
                                                 LocalDate dateFilter, Pageable pageable) {
        log.debug("Buscando historial paginado para {}", username);
        return favorRepository.findAll(
            crearEspecificacionFiltros(username, statusFilter, dateFilter), 
            pageable
        );
    }

    /**
     * Busca un favor por ID verificando que pertenezca al usuario
     */
    @Transactional(readOnly = true)
    public Favor findFavorByIdAndUsername(Integer idFavor, String username) {
        log.debug("Buscando favor #{} para usuario {}", idFavor, username);
        return favorRepository.findByIdfavorAndPersonaUsername(idFavor, username)
            .orElseThrow(() -> new EntityNotFoundException(
                "Favor no encontrado (#" + idFavor + ") o no pertenece al usuario."));
    }

    /**
     * Actualiza el estado de un favor
     */
    @Transactional
    public void actualizarEstadoFavor(Integer idFavor, EstadoFavor nuevoEstado) {
        log.info("Actualizando estado del favor #{} a {}", idFavor, nuevoEstado);
        Favor favor = findFavorById(idFavor);
        validarTransicionEstado(favor.getEstado(), nuevoEstado);
        favor.setEstado(nuevoEstado);
        favorRepository.save(favor);
        log.info("Estado del favor #{} actualizado.", idFavor);
    }

    /**
     * Busca un favor por su ID
     */
    @Transactional(readOnly = true)
    public Favor findFavorById(Integer idFavor) {
        return favorRepository.findById(idFavor)
            .orElseThrow(() -> new EntityNotFoundException("Favor no encontrado: " + idFavor));
    }

    // Métodos auxiliares privados

    /**
     * Guarda un favor y maneja posibles errores
     */
    private Favor guardarFavor(Favor favor, String username, String tipo) {
        try {
            Favor favorGuardado = favorRepository.save(favor);
            log.info("Favor {} #{} creado exitosamente para {}", 
                    tipo, favorGuardado.getIdfavor(), username);
            return favorGuardado;
        } catch (Exception e) {
            log.error("Error al guardar favor {} para {}: {}", 
                    tipo, username, e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar el favor (" + tipo + ").", e);
        }
    }

    /**
     * Obtiene un usuario por su username
     */
    private Persona obtenerPersona(String username) {
        return personaRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Obtiene una tienda por su ID
     */
    private Tienda obtenerTienda(Integer idTienda) {
        if (idTienda == null) {
            throw new IllegalArgumentException("ID de Tienda no puede ser nulo para crear favor desde tienda.");
        }
        return tiendaRepository.findById(idTienda)
            .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada: " + idTienda));
    }

    /**
     * Asigna un delivery al favor si se especifica
     */
    private void asignarDeliveryOpcional(Favor favor, Integer idDelivery) {
        if (idDelivery != null) {
            deliveryRepository.findById(idDelivery)
                .ifPresentOrElse(favor::setDelivery,
                    () -> log.warn("Delivery ID {} no encontrado al asignar.", idDelivery));
        }
    }

    /**
     * Procesa la lista de productos solicitados
     */
    private Set<FavorProducto> procesarItems(List<FavorProductoItemDTO> items, Favor favor) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La lista de items no puede ser nula o vacía para crear favor desde tienda.");
        }

        Set<FavorProducto> favorProductos = new HashSet<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (FavorProductoItemDTO itemDto : items) {
            if (itemDto.getCantidad() <= 0) {
                log.warn("Se ignoró item con cantidad inválida: Producto ID {}", itemDto.getIdProducto());
                continue;
            }

            Productos producto = productoRepository.findById(itemDto.getIdProducto())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + itemDto.getIdProducto()));

            valorTotal = valorTotal.add(
                producto.getPrecio().multiply(new BigDecimal(itemDto.getCantidad()))
            );

            favorProductos.add(crearFavorProducto(favor, producto, itemDto.getCantidad()));
        }

        favor.setValor(valorTotal);
        log.debug("Valor total calculado para favor: {}", valorTotal);
        return favorProductos;
    }

    /**
     * Crea un item de favor-producto
     */
    private FavorProducto crearFavorProducto(Favor favor, Productos producto, int cantidad) {
        FavorProducto favorProducto = new FavorProducto();
        favorProducto.setFavor(favor);
        favorProducto.setProducto(producto);
        favorProducto.setCantidad(cantidad);
        return favorProducto;
    }

    /**
     * Valida que el usuario tenga saldo suficiente
     */
    private void validarSaldo(Persona persona, BigDecimal valorFavor) {
        if (valorFavor == null) {
            log.error("Validación Saldo: Valor Nulo");
            throw new IllegalArgumentException("El valor del favor es nulo.");
        }
        if (persona.getSaldo().compareTo(valorFavor) < 0) {
            log.warn("Validación Saldo: Insuficiente para {}. Saldo={}, Costo={}", 
                    persona.getUsername(), persona.getSaldo(), valorFavor);
            throw new IllegalStateException(
                String.format("Saldo insuficiente. Saldo: %s, Costo: %s", 
                    persona.getSaldo(), valorFavor));
        }
        log.debug("Validación Saldo: OK para {}.", persona.getUsername());
    }

    /**
     * Valida que la transición de estado sea válida
     */
    private void validarTransicionEstado(EstadoFavor estadoActual, EstadoFavor nuevoEstado) {
        log.debug("Validando transición {}->{}", estadoActual, nuevoEstado);
    }

    /**
     * Crea especificación de filtros para búsqueda
     */
    private Specification<Favor> crearEspecificacionFiltros(String username, String statusFilter, LocalDate dateFilter) {
        return (root, query, cb) -> {
            var predicate = cb.equal(root.get("persona").get("username"), username);
            
            if (statusFilter != null && !statusFilter.equalsIgnoreCase("all")) {
                try {
                    EstadoFavor estado = EstadoFavor.valueOf(statusFilter.toUpperCase());
                    predicate = cb.and(predicate, cb.equal(root.get("estado"), estado));
                } catch (IllegalArgumentException ignored) {}
            }
            
            if (dateFilter != null) {
                predicate = cb.and(predicate, 
                    cb.equal(cb.function("DATE", LocalDate.class, root.get("fechaCreacion")), 
                    dateFilter));
            }
            
            return predicate;
        };
    }
}