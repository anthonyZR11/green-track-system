
# Clonar el Repositorio

```bash
    git clone https://github.com/anthonyZR11/green-track-system.git

    Para backend -  cd /backend
    Para frontend -  cd /frontend
```

# Green Track Backend

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security con JWT para autenticación y autorización
- Spring Data JPA con Hibernate
- MySQL
- Swagger UI para documentación y pruebas

## Requisitos

- JDK 17
- Maven
- IntelliJ IDEA

## Pasos para ejecutar

1. Abrir IntelliJ IDEA y seleccionar `Open` para abrir la carpeta del proyecto.
2. Esperar que IntelliJ importe dependencias (Maven).
3. Configurar el archivo `src/main/resources/application.properties` con tu conexión a la base de datos.
4. Ejecutar la clase principal con `Run`.
5. Acceder a `http://localhost:8080/api` para el API REST.
   - Rutas de autenticación: `/auth/**`
   - Rutas protegidas con token JWT: `/v1/**`
6. Para pruebas o desarrollo, usar Swagger UI en `http://localhost:8080/swagger-ui.html`.


## Configuración adicional

- Claves JWT configuradas en `application.properties`:
  - `jwt.secret`
  - `jwt.expiration`



# Green Track Frontend

Este proyecto es el frontend de la aplicación Green Track, desarrollada con Angular.

## Requisitos Previos

Antes de levantar el proyecto, asegúrate de tener instalados los siguientes softwares:

*   **Node.js y npm**: Puedes descargarlos desde la [página oficial de Node.js](https://nodejs.org/). Se recomienda usar una versión LTS.
*   **Angular CLI**: Instálalo globalmente usando npm:
    ```bash
    npm install -g @angular/cli@17

    ```

## Configuración del Proyecto

1.  **Instalar Dependencias**:
    Navega a la carpeta `frontend` del proyecto e instala todas las dependencias necesarias:
    ```bash
    npm install --legacy-peer-deps
    ```

2.  **Configurar el Entorno (API URL)**:
    El frontend se comunica con un backend a través de una API. La URL de la API se configura en el archivo `src/app/environments/enviroment.ts`.
    Abre este archivo y asegúrate de que `apiUrl` apunte a la dirección correcta de tu backend (por defecto, es `http://localhost:8080/api`). Acompañado de /v1 para las rutas users, equipments y loans

    ```typescript
    // src/app/environments/enviroment.ts
    export const environment = {
      production: false,
      apiUrl: 'http://localhost:8080/api' // Asegúrate de que esta URL sea correcta
    };
    ```

## Ejecutar el Proyecto

Una vez que las dependencias estén instaladas y el entorno configurado, puedes levantar el servidor de desarrollo:

```bash
npm run start
# O usando Angular CLI directamente si está en tu PATH
# ng serve
```

Esto iniciará el servidor de desarrollo en `http://localhost:4200/`. La aplicación se recargará automáticamente si cambias alguno de los archivos fuente.

## Vistas Implementadas

El proyecto incluye las siguientes vistas principales:

*   **Login**: `/login`
*   **Registro**: `/registro`
*   **Home (Estadísticas)**: `/home` (muestra el total de usuarios, equipos disponibles y préstamos activos)
*   **Equipos**: `/equipos` (CRUD completo con filtros por tipo, marca y estado)
*   **Préstamos**: `/prestamos` (CRUD con filtros por usuario y rango de fechas, campo de equipo como selector y botón de devolución)
*   **Usuarios**: `/usuarios` (CRUD con campos de nombre, usuario, email, rol y contraseña opcional al editar)
*   **Gráficos**: `/graficos` (vista con un gráfico de pastel para el estado de los equipos y prestamos)

## Construir para Producción

Para construir el proyecto para producción, utiliza el siguiente comando:

```bash
npm run build
# O
# ng build
```
