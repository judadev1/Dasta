package juda.dev.dastaprueba1.controller;

import jakarta.validation.Valid;
import juda.dev.dastaprueba1.DTO.PasswordChangeDTO;
import juda.dev.dastaprueba1.entity.Persona;
import juda.dev.dastaprueba1.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    // Logger para registro de eventos
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    
    // Servicio para gestión de usuarios
    private final PersonaService personaService;

    /**
     * Muestra la página de perfil con información del usuario
     * Utiliza Thymeleaf para mostrar datos personales y formulario de cambio de contraseña
     */
    @GetMapping
    public String viewProfile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        
        // Carga datos del usuario
        String username = authentication.getName();
        Persona persona = personaService.findByUsername(username);
        model.addAttribute("persona", persona);
        
        // Inicializa formulario de cambio de contraseña si no existe
        if (!model.containsAttribute("passwordChangeDTO")) {
            model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        }
        return "profile";
    }

    /**
     * Procesa la solicitud de cambio de contraseña
     * Valida que las contraseñas coincidan y actualiza si todo es correcto
     */
    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordChangeDTO") PasswordChangeDTO passwordChangeDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        String currentUsername = authentication.getName();

        // Valida que las contraseñas nuevas coincidan
        if (passwordChangeDTO.getNewPassword() != null &&
                !passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.passwordChangeDTO", "Las contraseñas nuevas no coinciden.");
        }

        // Si hay errores, vuelve al formulario
        if (result.hasErrors()) {
            log.warn("Errores al validar cambio de contraseña para {}: {}", currentUsername, result.getAllErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChangeDTO", result);
            redirectAttributes.addFlashAttribute("passwordChangeDTO", passwordChangeDTO);
            return "redirect:/profile";
        }

        // Intenta actualizar la contraseña
        try {
            personaService.changePassword(currentUsername, passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Contraseña actualizada con éxito.");
        } catch (IllegalArgumentException e) {
            // Error de validación (ej: contraseña actual incorrecta)
            log.warn("Fallo cambio de contraseña para {}: {}", currentUsername, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("passwordChangeDTO", passwordChangeDTO);
        } catch (Exception e) {
            // Error inesperado
            log.error("Error inesperado al cambiar contraseña para {}: ", currentUsername, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error inesperado al cambiar la contraseña.");
            redirectAttributes.addFlashAttribute("passwordChangeDTO", passwordChangeDTO);
        }
        return "redirect:/profile";
    }
}