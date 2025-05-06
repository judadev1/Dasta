
#### Dasta - Plataforma de Favores
### Juan David Arboleda Vallecilla
## Tecnología en Desarrollo de Software
# Semestre 4

## Requisitos Previos

- [Docker](https://www.docker.com/products/docker-desktop/)
- [Docker Compose](https://docs.docker.com/compose/install/) (viene incluido en Docker Desktop para Windows y Mac)
- [Git](https://git-scm.com/downloads)

## Instalación y Ejecución

### Windows

1. Instala Docker Desktop desde [aquí](https://www.docker.com/products/docker-desktop/)
2. Clona el repositorio:
   ```bash
   git clone https://github.com/judadev1/Dasta
   cd Dasta
   ```
3. Abre PowerShell o CMD como administrador y ejecuta:
   ```bash
   docker-compose up --build
   ```
4. Espera a que todos los servicios estén iniciados (~2-3 minutos)
5. Accede a la aplicación en: http://localhost:8080
6. Para ver la base de datos, accede a Adminer en: http://localhost:8081

### Linux

1. Instala Docker:
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install docker.io docker-compose

   # Fedora
   sudo dnf install docker docker-compose
   ```
2. Inicia el servicio de Docker:
   ```bash
   sudo systemctl start docker
   sudo systemctl enable docker
   ```
3. Agrega tu usuario al grupo docker:
   ```bash
   sudo usermod -aG docker $USER
   # Cierra sesión y vuelve a iniciar para que los cambios surtan efecto
   ```
4. Clona y ejecuta el proyecto:
   ```bash
   git clone https://github.com/judadev1/Dasta
   cd Dasta
   docker-compose up --build
   ```

### macOS

1. Instala Docker Desktop desde [aquí](https://www.docker.com/products/docker-desktop/)
2. Abre Terminal y ejecuta:
   ```bash
   git clone https://github.com/judadev1/Dasta
   cd Dasta
   docker-compose up --build
   ```

## Verificación de la Instalación

1. La aplicación estará disponible en:
    - Frontend: http://localhost:8080
    - Base de datos (Adminer): http://localhost:8081

2. Credenciales de Adminer:
    - Sistema: MySQL
    - Servidor: mysql
    - Usuario: springuser
    - Contraseña: springpassword
    - Base de datos: favores_db

## Detener la Aplicación

Para detener la aplicación, presiona `Ctrl+C` en la terminal donde está ejecutándose, o abre otra terminal y ejecuta:
```bash
docker-compose down
```