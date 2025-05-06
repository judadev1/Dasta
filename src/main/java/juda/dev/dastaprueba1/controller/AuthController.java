package juda.dev.dastaprueba1.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import juda.dev.dastaprueba1.DTO.RegistroDTO;
import juda.dev.dastaprueba1.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador que maneja la autenticación y registro de usuarios
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    // Logger para registro de eventos
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    // Servicio para gestión de usuarios
    private final PersonaService personaService;

    /**
     * Muestra página de login/registro
     * Inicializa formulario si no existe
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        // Prepara formulario vacío si no existe
        if (!model.containsAttribute("registroDTO")) {
            model.addAttribute("registroDTO", new RegistroDTO());
        }
        return "login";
    }

    /**
     * Procesa registro de nuevos usuarios
     * Valida datos y gestiona errores
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registroDTO") RegistroDTO registroDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        // Valida coincidencia de contraseñas
        if (registroDTO.getPassword() != null && !registroDTO.getPassword().isEmpty() &&
                !registroDTO.getPassword().equals(registroDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.registroDTO", "Las contraseñas no coinciden");
        }

        // Verifica si el email ya está registrado
        if (registroDTO.getEmail() != null && !registroDTO.getEmail().isBlank() &&
                personaService.findByUsername(registroDTO.getEmail()) != null) {
            result.rejectValue("email", "error.registroDTO", "El correo electrónico ya está registrado");
        }

        // Si hay errores, vuelve al formulario
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registroDTO", result);
            redirectAttributes.addFlashAttribute("registroDTO", registroDTO);
            log.warn("Errores de validación en registro: {}", result.getAllErrors());
            return "redirect:/login?errorRegistro";
        }

        // Intenta registrar al usuario
        try {
            personaService.registrarNuevaPersona(registroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "¡Registro exitoso! Ya puedes iniciar sesión.");
            return "redirect:/login";

        // Manejo de errores específicos
        } catch (IllegalArgumentException | EntityExistsException e) {
            log.warn("Error de negocio al registrar [{}]: {}", registroDTO.getEmail(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("registroDTO", registroDTO);
            return "redirect:/login?errorRegistro";
            
        // Manejo de errores inesperados
        } catch (Exception e) {
            log.error("Error inesperado durante el registro para [{}]: {}", registroDTO.getEmail(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado durante el registro. Inténtalo de nuevo.");
            redirectAttributes.addFlashAttribute("registroDTO", registroDTO);
            return "redirect:/login?errorRegistro";
        }
    }
}