// Esperar a que el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", () => {
  // Inicializar tabs para login/registro
  initAuthTabs()

  // Inicializar calificación con estrellas
  initRatingStars()

  // Añadir clases a elementos existentes para mejorar el diseño
  enhanceDesign()

  // Añadir animaciones de entrada
  addFadeInAnimations()

  // Añadir elementos decorativos
  addDecorativeElements()

  // Inicializar tooltips de Bootstrap
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  var tooltipList = tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))

  // Configurar el modal de tiendas
  setupTiendasModal()

  // Configurar formularios de login y registro
  setupAuthForms()

  // Añadir efectos de brillo a elementos interactivos
  addGlowEffects()
})

// Función para inicializar tabs de autenticación
function initAuthTabs() {
  const loginTab = document.getElementById("login-tab")
  const registerTab = document.getElementById("register-tab")
  const loginContent = document.getElementById("login-content")
  const registerContent = document.getElementById("register-content")

  if (loginTab && registerTab && loginContent && registerContent) {
    loginTab.addEventListener("click", () => {
      loginTab.classList.add("active")
      registerTab.classList.remove("active")
      loginContent.classList.add("active")
      registerContent.classList.remove("active")
    })

    registerTab.addEventListener("click", () => {
      registerTab.classList.add("active")
      loginTab.classList.remove("active")
      registerContent.classList.add("active")
      loginContent.classList.remove("active")
    })
  }
}

// Función para configurar formularios de autenticación
function setupAuthForms() {
  const loginForm = document.getElementById("login-form")
  if (loginForm) {
    loginForm.addEventListener("submit", (e) => {
      e.preventDefault()

      const email = document.getElementById("login-email").value
      const password = document.getElementById("login-password").value

      if (!email || !password) {
        alert("Por favor, completa todos los campos.")
        return
      }

      // Simulación de login exitoso
      window.location.href = "index.html"
    })
  }

  const registerForm = document.getElementById("register-form")
  if (registerForm) {
    registerForm.addEventListener("submit", (e) => {
      e.preventDefault()

      const email = document.getElementById("register-email").value
      const password = document.getElementById("register-password").value
      const confirmPassword = document.getElementById("register-confirm-password").value
      const terms = document.getElementById("terms").checked

      if (!email || !password || !confirmPassword) {
        alert("Por favor, completa todos los campos obligatorios.")
        return
      }

      if (password !== confirmPassword) {
        alert("Las contraseñas no coinciden.")
        return
      }

      if (!terms) {
        alert("Debes aceptar los términos y condiciones.")
        return
      }

      // Simulación de registro exitoso
      window.location.href = "index.html"
    })
  }
}

// Función para inicializar calificación con estrellas
function initRatingStars() {
  const ratingInput = document.getElementById("nota")
  const ratingStars = document.querySelectorAll(".rating input")

  if (ratingInput && ratingStars.length > 0) {
    // Agregar eventos a las estrellas
    ratingStars.forEach((star) => {
      star.addEventListener("change", () => {
        ratingInput.value = star.value

        // Añadir efecto visual
        const starValue = star.value
        const stars = document.querySelectorAll(".rating label")

        stars.forEach((starLabel, index) => {
          if (index < 5 - starValue) {
            starLabel.classList.remove("active")
          } else {
            starLabel.classList.add("active")

            // Añadir animación de pulso
            starLabel.animate([{ transform: "scale(1)" }, { transform: "scale(1.2)" }, { transform: "scale(1)" }], {
              duration: 300,
              iterations: 1,
            })
          }
        })
      })
    })

    // Si ya hay un valor en el input, marcar las estrellas correspondientes
    if (ratingInput.value) {
      const activeStar = document.querySelector(`.rating input[value="${ratingInput.value}"]`)
      if (activeStar) {
        activeStar.checked = true
      }
    }
  }
}

// Función para mejorar el diseño de elementos existentes
function enhanceDesign() {
  // Mejorar tablas
  document.querySelectorAll("table").forEach((table) => {
    if (!table.classList.contains("enhanced")) {
      table.classList.add("enhanced")
    }
  })

  // Mejorar botones
  document.querySelectorAll("button").forEach((button) => {
    if (!button.classList.contains("btn")) {
      if (button.type === "submit") {
        button.classList.add("btn", "btn-primary")
      } else {
        button.classList.add("btn", "btn-outline-light")
      }
    }
  })

  // Mejorar enlaces
  document.querySelectorAll("a").forEach((link) => {
    if (
        !link.classList.contains("btn") &&
        !link.classList.contains("nav-link") &&
        !link.classList.contains("store-link")
    ) {
      link.classList.add("text-info")
    }
  })

  // Mejorar mensajes de error y éxito
  document.querySelectorAll("div[style*='color: red']").forEach((div) => {
    div.classList.add("alert", "alert-danger")
    div.removeAttribute("style")
  })

  document.querySelectorAll("div[style*='color: green']").forEach((div) => {
    div.classList.add("alert", "alert-success")
    div.removeAttribute("style")
  })

  // Añadir iconos a los estados de los favores
  document.querySelectorAll("td").forEach((td) => {
    if (td.textContent.trim() === "PENDIENTE") {
      td.innerHTML = '<span class="badge badge-warning">PENDIENTE</span>'
    } else if (td.textContent.trim() === "EN_PROGRESO") {
      td.innerHTML = '<span class="badge badge-primary">EN PROGRESO</span>'
    } else if (td.textContent.trim() === "ENTREGADO") {
      td.innerHTML = '<span class="badge badge-success">ENTREGADO</span>'
    } else if (td.textContent.trim() === "CANCELADO") {
      td.innerHTML = '<span class="badge badge-danger">CANCELADO</span>'
    }
  })

  // Mejorar cards
  document.querySelectorAll(".card").forEach((card) => {
    if (!card.classList.contains("enhanced")) {
      card.classList.add("enhanced", "glow-effect")
    }
  })
}

// Función para añadir animaciones de entrada
function addFadeInAnimations() {
  // Añadir animación a elementos principales
  const main = document.querySelector("main")
  if (main && !main.classList.contains("fade-in")) {
    main.classList.add("fade-in")
  }

  // Añadir animación a cards con retraso escalonado
  document.querySelectorAll(".card").forEach((card, index) => {
    if (!card.classList.contains("fade-in")) {
      card.style.animationDelay = `${index * 0.15}s`
      card.classList.add("fade-in")
    }
  })

  // Añadir animación a elementos de información
  document.querySelectorAll(".info-item").forEach((item, index) => {
    if (!item.classList.contains("fade-in")) {
      item.style.animationDelay = `${0.3 + index * 0.1}s`
      item.classList.add("fade-in")
    }
  })
}

// Función para añadir elementos decorativos
function addDecorativeElements() {
  // Añadir círculos decorativos
  const main = document.querySelector("main")
  if (main) {
    const circle1 = document.createElement("div")
    circle1.className = "decorative-circle circle-1"
    main.appendChild(circle1)

    const circle2 = document.createElement("div")
    circle2.className = "decorative-circle circle-2"
    main.appendChild(circle2)
  }

  // Añadir iconos decorativos si no existen
  if (!document.querySelector(".decorative-icon.top-right")) {
    const iconTopRight = document.createElement("i")
    iconTopRight.className = "bi bi-box-seam decorative-icon top-right"
    document.body.appendChild(iconTopRight)
  }

  if (!document.querySelector(".decorative-icon.bottom-left")) {
    const iconBottomLeft = document.createElement("i")
    iconBottomLeft.className = "bi bi-bicycle decorative-icon bottom-left"
    document.body.appendChild(iconBottomLeft)
  }
}

// Función para añadir efectos de brillo a elementos interactivos
function addGlowEffects() {
  // Añadir efecto de brillo a botones
  document.querySelectorAll(".btn").forEach((btn) => {
    if (!btn.classList.contains("glow-effect")) {
      btn.classList.add("glow-effect")
    }
  })

  // Añadir efecto de brillo a tarjetas de tiendas
  document.querySelectorAll(".store-card").forEach((card) => {
    if (!card.classList.contains("glow-effect")) {
      card.classList.add("glow-effect")
    }
  })

  // Añadir efecto de brillo a tarjetas de productos
  document.querySelectorAll(".product-card").forEach((card) => {
    if (!card.classList.contains("glow-effect")) {
      card.classList.add("glow-effect")
    }
  })
}

// Dummy bootstrap variable declaration
const bootstrap = window.bootstrap
// Función para aumentar cantidad de producto
function increaseQuantity(inputId) {
  console.log("Attempting to increase:", inputId);
  const input = document.getElementById(inputId);
  if (input) {
    let currentValue = Number.parseInt(input.value || 0);
    input.value = currentValue + 1;
    console.log(`Increased ${inputId} to ${input.value}`);
    // Disparar evento 'change' si es necesario para otras lógicas o validaciones JS
    input.dispatchEvent(new Event('change', { bubbles: true }));
  } else {
    console.error(`Input not found for increase: ${inputId}`);
  }
}

// Función para disminuir cantidad de producto
function decreaseQuantity(inputId) {
  console.log("Attempting to decrease:", inputId);
  const input = document.getElementById(inputId);
  if (input) {
    let currentValue = Number.parseInt(input.value || 0);
    if (currentValue > 0) {
      input.value = currentValue - 1;
      console.log(`Decreased ${inputId} to ${input.value}`);
      // Disparar evento 'change' si es necesario
      input.dispatchEvent(new Event('change', { bubbles: true }));
    }
  } else {
    console.error(`Input not found for decrease: ${inputId}`);
  }
}

// Función para manejar la selección visual de tarjetas de delivery
function selectDeliveryCard(selectedLabel) {
  console.log("Selecting delivery card...");
  const deliveryOptionsContainer = document.getElementById('delivery-options');
  if (!deliveryOptionsContainer) return; // Salir si no estamos en la página correcta

  // Quitar clase 'selected' de todas las tarjetas
  deliveryOptionsContainer.querySelectorAll('.delivery-card').forEach(label => {
    label.classList.remove('selected');
  });
  // Añadir clase 'selected' a la tarjeta clickeada
  selectedLabel.classList.add('selected');
  // Asegurarse de que el radio button dentro de la label esté seleccionado
  const radioInput = selectedLabel.querySelector('input[type="radio"]');
  if (radioInput) {
    // Marcar el radio lógicamente (aunque th:field debería hacerlo)
    radioInput.checked = true;
    console.log(`Selected delivery card for value: ${radioInput.value}`);
  } else {
    console.error("Radio input not found within selected delivery card label.");
  }
}

// Lógica específica para inicializar la página seleccionar-productos.html
function initSeleccionarProductosPage() {
  console.log("Running initSeleccionarProductosPage()...");

  const deliveryOptionsContainer = document.getElementById('delivery-options');
  if (deliveryOptionsContainer) {
    // Seleccionar visualmente la tarjeta de delivery correcta al cargar
    const selectedRadio = deliveryOptionsContainer.querySelector('input[name="idDelivery"]:checked');
    if (selectedRadio) {
      console.log(`Initial selected delivery value: ${selectedRadio.value}`);
      const selectedLabel = selectedRadio.closest('.delivery-card');
      if (selectedLabel) {
        deliveryOptionsContainer.querySelectorAll('.delivery-card').forEach(label => { label.classList.remove('selected'); });
        selectedLabel.classList.add('selected');
        console.log("Applied 'selected' class to initial delivery card.");
      } else {
        console.warn("Could not find label for initially checked radio.");
      }
    } else {
      console.log("No initial delivery selected or radio group not found.");
    }

    // Diagnóstico botones +/-
    document.querySelectorAll('.product-quantity button').forEach(button => {
      const hasOnClick = button.hasAttribute('onclick');
      console.log(`Button check: ${button.textContent} has onclick: ${hasOnClick}`);
      if (!hasOnClick) {
        console.error("Button +/- is missing onclick attribute!", button);
      }
    });
  } else {
    console.log("Delivery options container not found. Skipping delivery card init.");
  }
}

// --- Listener Principal DOMContentLoaded (Revisado) ---
document.addEventListener("DOMContentLoaded", () => {
  console.log("Generic DOMContentLoaded fired.");

  // --- Inicialización específica de página ---
  // Verifica si estamos en seleccionar-productos.html buscando elementos únicos
  if (document.querySelector('.product-grid') && document.getElementById('delivery-options')) {
    initSeleccionarProductosPage();
  } else {
    console.log("Not on seleccionar-productos page or elements missing.");
  }
  if (document.getElementById('login-tab')) {
    initAuthTabs(); // Para login.html
  }
  if (document.querySelector('.rating input')) {
    initRatingStars(); // Para calificar-favor.html
  }

  // Inicializar tooltips de Bootstrap (seguro)
  try {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
      return new bootstrap.Tooltip(tooltipTriggerEl)
    });
    console.log("Bootstrap Tooltips initialized.");
  } catch (e) {
    console.error("Error initializing Bootstrap Tooltips:", e);
  }

});

// Función para inicializar tabs de autenticación (Ejemplo de tu JS)
function initAuthTabs() {
  const loginTab = document.getElementById("login-tab");
  const registerTab = document.getElementById("register-tab");
  const loginContent = document.getElementById("login-content");
  const registerContent = document.getElementById("register-content");

  if (loginTab && registerTab && loginContent && registerContent) {
    loginTab.addEventListener("click", () => {
      loginTab.classList.add("active");
      registerTab.classList.remove("active");
      loginContent.classList.add("active");
      registerContent.classList.remove("active");
    });

    registerTab.addEventListener("click", () => {
      registerTab.classList.add("active");
      loginTab.classList.remove("active");
      registerContent.classList.add("active");
      loginContent.classList.remove("active");
    });
    console.log("Auth Tabs Initialized.");
  }
}

// Función para inicializar calificación con estrellas (Ejemplo de tu JS)
function initRatingStars() {
  const ratingInput = document.getElementById("nota"); // El input hidden que guarda el valor
  const ratingStarsRadios = document.querySelectorAll(".rating input[type='radio']"); // Los radios de las estrellas

  if (ratingInput && ratingStarsRadios.length > 0) {
    console.log("Initializing rating stars...");
    ratingStarsRadios.forEach((starRadio) => {
      starRadio.addEventListener("change", () => {
        if (starRadio.checked) {
          ratingInput.value = starRadio.value; // Actualiza el input hidden
          console.log("Rating changed to:", ratingInput.value);
        }
        // El estilo visual lo maneja el CSS con :checked ~ label
      });
    });

    // Marcar visualmente al inicio si ya hay un valor (opcional, CSS debería bastar)
    const initialValue = ratingInput.value;
    if (initialValue) {
      const initialCheckedRadio = document.querySelector(`.rating input[type='radio'][value='${initialValue}']`);
      if (initialCheckedRadio) {
        // initialCheckedRadio.checked = true; // Esto ya debería estar por th:field
        console.log("Initial rating value set:", initialValue);
      }
    }
  }
}