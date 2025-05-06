// Controlador principal para gestionar todas las operaciones relacionadas con favores
package juda.dev.dastaprueba1.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import juda.dev.dastaprueba1.DTO.*;
import juda.dev.dastaprueba1.entity.*;
import juda.dev.dastaprueba1.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FavorController {

    // Logger para registro de eventos
    private static final Logger log = LoggerFactory.getLogger(FavorController.class);
    
    // Servicios necesarios mediante inyección de dependencias
    private final FavorService favorService;
    private final TiendaService tiendaService;
    private final DeliveryService deliveryService;
    private final PersonaService personaService;
    private final ProductoService productoService;

    /**
     * Muestra la página principal y carga datos según el usuario
     */
    @GetMapping("/")
    public String showIndex(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            log.info("Usuario '{}' accediendo a la página principal", username);
            // Carga información del usuario y datos necesarios
            Persona persona = personaService.findByUsername(username);
            model.addAttribute("persona", persona);
            model.addAttribute("tiendas", tiendaService.getAllTiendas());
            model.addAttribute("deliveries", deliveryService.getAllDeliveries());
            // Inicializa formulario de favor simple si no existe
            if (!model.containsAttribute("favorSimpleRequest")) {
                model.addAttribute("favorSimpleRequest", new FavorSimpleRequestDTO());
            }
        }
        return "index";
    }

    /**
     * Procesa la solicitud de un favor simple
     */
    @PostMapping("/favores/pedir/simple")
    public String submitFavorSimple(@Valid @ModelAttribute("favorSimpleRequest") FavorSimpleRequestDTO favorRequest,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Authentication authentication) {
        // Verifica autenticación
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        
        // Procesa el favor si no hay errores
        String currentUsername = authentication.getName();
        if (result.hasErrors()) {
            log.warn("Errores validación favor simple {}: {}", currentUsername, result.getAllErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.favorSimpleRequest", result);
            redirectAttributes.addFlashAttribute("favorSimpleRequest", favorRequest);
            return "redirect:/";
        }
        
        // Intenta crear el favor
        try {
            favorService.crearFavorSimple(currentUsername, favorRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Favor solicitado!");
            return "redirect:/favores/historial";
        } catch (Exception e) {
            log.error("Error creando favor simple {}: {}", currentUsername, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("favorSimpleRequest", favorRequest);
            return "redirect:/";
        }
    }

    /**
     * Muestra página para seleccionar tienda
     */
    @GetMapping("/favores/nuevo/seleccionar-tienda")
    public String showSeleccionarTienda(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        // Carga datos necesarios
        String username = authentication.getName();
        Persona persona = personaService.findByUsername(username);
        model.addAttribute("persona", persona);
        model.addAttribute("tiendas", tiendaService.getAllTiendas());
        return "seleccionar-tienda";
    }

    /**
     * Muestra productos de una tienda específica
     */
    @GetMapping("/favores/nuevo/seleccionar-productos")
    public String showSeleccionarProductos(@RequestParam("tiendaId") Integer tiendaId, 
                                         Model model, 
                                         Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        
        try {
            // Carga información necesaria
            String username = authentication.getName();
            Persona persona = personaService.findByUsername(username);
            Tienda tienda = tiendaService.findById(tiendaId);
            List<Productos> productos = productoService.getProductosPorTienda(tiendaId);
            List<Delivery> deliveries = deliveryService.getAllDeliveries();
            
            // Agrega atributos al modelo
            model.addAttribute("persona", persona);
            model.addAttribute("tienda", tienda);
            model.addAttribute("productos", productos);
            model.addAttribute("deliveries", deliveries);
            
            // Prepara formulario
            if (!model.containsAttribute("favorStoreRequest")) {
                FavorStoreRequestDTO requestDTO = new FavorStoreRequestDTO();
                requestDTO.setIdTienda(tiendaId);
                model.addAttribute("favorStoreRequest", requestDTO);
            } else {
                ((FavorStoreRequestDTO) model.getAttribute("favorStoreRequest")).setIdTienda(tiendaId);
            }
            return "seleccionar-productos";
            
        } catch (Exception e) {
            log.error("Error cargando productos tienda {}: {}", tiendaId, e.getMessage());
            return "redirect:/favores/nuevo/seleccionar-tienda?error";
        }
    }

    /**
     * Procesa la creación de un favor desde una tienda
     */
    @PostMapping("/favores/crear/tienda")
    public String crearFavorDesdeTienda(
            @ModelAttribute("favorStoreRequest") FavorStoreRequestDTO favorRequest,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        
        String currentUsername = authentication.getName();
        Integer tiendaId = favorRequest.getIdTienda();
        
        // Valida datos del formulario
        DataBinder dataBinder = new DataBinder(favorRequest);
        BindingResult bindingResult = dataBinder.getBindingResult();
        List<FavorProductoItemDTO> itemsSeleccionados = extraerItems(request, bindingResult);
        favorRequest.setItems(itemsSeleccionados);

        // Validaciones adicionales
        if (itemsSeleccionados.isEmpty()) {
            bindingResult.rejectValue("items", "NotEmpty", "Selecciona productos.");
        }
        if (favorRequest.getDireccion() == null || favorRequest.getDireccion().isBlank()) {
            bindingResult.rejectValue("direccion", "NotBlank", "Dirección obligatoria.");
        }

        // Manejo de errores
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.favorStoreRequest", bindingResult);
            redirectAttributes.addFlashAttribute("favorStoreRequest", favorRequest);
            return "redirect:/favores/nuevo/seleccionar-productos?tiendaId=" + tiendaId;
        }

        // Crea el favor
        try {
            Favor nuevoFavor = favorService.crearFavorDesdeTienda(currentUsername, favorRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Favor solicitado!");
            return "redirect:/favores/calificar/" + nuevoFavor.getIdfavor();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("favorStoreRequest", favorRequest);
            return "redirect:/favores/nuevo/seleccionar-productos?tiendaId=" + tiendaId;
        }
    }

    /**
     * Muestra página para calificar un favor
     */
    @GetMapping("/favores/calificar/{idfavor}")
    public String showCalificarFavor(@PathVariable("idfavor") Integer idfavor, 
                                   Model model, 
                                   Authentication authentication, 
                                   RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        
        String currentUsername = authentication.getName();
        try {
            // Verifica y carga datos del favor
            Favor favor = favorService.findFavorByIdAndUsername(idfavor, currentUsername);
            Persona persona = personaService.findByUsername(currentUsername);
            model.addAttribute("persona", persona);
            
            // Validaciones de estado
            if (favor.getDelivery() == null) return "redirect:/favores/historial";
            if (favor.getCalificacion() != null) return "redirect:/favores/historial";

            // Prepara formulario de calificación
            model.addAttribute("favor", favor);
            if (!model.containsAttribute("calificacionDTO")) {
                model.addAttribute("calificacionDTO", new CalificacionDTO());
            }
            return "calificar-favor";
            
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Favor no encontrado.");
            return "redirect:/favores/historial";
        } catch (Exception e) {
            log.error("Error al mostrar calificación favor #{}: {}", idfavor, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado.");
            return "redirect:/favores/historial";
        }
    }

    /**
     * Muestra el historial de favores del usuario
     */
    @GetMapping("/favores/historial")
    public String showHistorial(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        
        // Carga datos del usuario y sus favores
        String currentUsername = authentication.getName();
        Persona persona = personaService.findByUsername(currentUsername);
        model.addAttribute("persona", persona);
        List<Favor> favores = favorService.getFavoresPorUsuario(currentUsername);
        model.addAttribute("favores", favores);
        model.addAttribute("calificacionDTO", new CalificacionDTO());
        return "history";
    }

    /**
     * Extrae y procesa items seleccionados del formulario
     */
    private List<FavorProductoItemDTO> extraerItems(HttpServletRequest request, 
                                                  BindingResult bindingResult) {
        List<FavorProductoItemDTO> items = new ArrayList<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        // Procesa cada item del formulario
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getKey().startsWith("quantity_")) {
                try {
                    int cantidad = Integer.parseInt(entry.getValue()[0]);
                    if (cantidad > 0) {
                        Integer productoId = Integer.parseInt(entry.getKey().substring("quantity_".length()));
                        items.add(new FavorProductoItemDTO(productoId, cantidad));
                    } else if (cantidad < 0) {
                        bindingResult.reject("items", "La cantidad no puede ser negativa.");
                    }
                } catch (Exception e) {
                    log.warn("Error parseando item {}: {}", entry.getKey(), e.getMessage());
                    bindingResult.reject("items", "Cantidad inválida detectada.");
                }
            }
        }
        return items;
    }
}