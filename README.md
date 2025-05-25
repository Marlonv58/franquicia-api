# Franquicia API

API REST construida con **Spring Boot + WebFlux**, para gestionar franquicias, sucursales y productos.  
Esta solución incluye programación reactiva, pruebas unitarias, despliegue automatizado con **Docker**, **Terraform** y ejecución en **AWS EC2**.

---

# Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.0 + WebFlux
- MySQL 8
- Docker + Docker Compose
- Terraform + AWS EC2 + Amazon RDS
- Swagger UI (OpenAPI)
- JUnit + Mockito
- Maven Wrapper
- GitHub Actions (opcional para CI/CD futuro)

---

# Estructura general del proyecto

```
.
├── src/
│   ├── main/java/com/franchise/api/...
│   └── test/java/com/franchise/api/...
├── Dockerfile
├── docker-compose.local.yml
├── terraform/
│   ├── main.tf
│   ├── rds-franquicia.tf
│   └── output.tf
├── pom.xml
├── .gitignore
└── README.md
```

---

# Pruebas unitarias

La lógica de negocio de servicios fue probada con `@ExtendWith(MockitoExtension.class)`, incluyendo:

- `BranchService`
    - `addBranch(...)`
    - `updateBranchName(...)`
- `ProductService`
    - `addProduct(...)`
    - `updateStock(...)`
- `FranchiseService`
    - `createFranchise(...)`
    - `updateFranchise(...)`

Ejecutar localmente:

```bash
    ./mvnw test
```

---

# Construcción automática del contenedor

Se usa un **Dockerfile multietapa**, donde la imagen final se genera desde cero en el EC2:

# `Dockerfile` (resumen)

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

No es necesario subir el `.jar`, ya que se construye automáticamente desde el código fuente.

---

# Ejecutar en entorno local (desarrollo)

Este proyecto incluye un `docker-compose.local.yml` que permite levantar toda la aplicación localmente sin necesidad de instalar MySQL, usar un IDE o crear la base de datos manualmente.

# Requisitos:

- Docker y Docker Compose instalados.

# Comando:

```bash
    docker compose -f docker-compose.local.yml up --build
```

# Resultado:

- Se levanta un contenedor de MySQL con:
  - Base de datos: `franquicia_db`
  - Usuario: `user`
  - Contraseña: `pass`
- Se levanta la API en `http://localhost:8080`
- Accede a la documentación: `http://localhost:8080/swagger-ui.html`

---

# Ejecutar en entorno productivo (AWS con Terraform)

El entorno de producción se levanta automáticamente en una instancia EC2 en AWS y se conecta con una base de datos MySQL en RDS.

# Requisitos:

- Tener configurado el CLI de AWS:
```bash
  aws configure
```
- Tener instalado terraform:
```bash
  https://developer.hashicorp.com/terraform/downloads
```
# Comandos Terraform:

``` bash
  cd terraform
  terraform init
  terraform apply
```

# ¿Qué hace esto?

- Crea una instancia EC2 (Amazon Linux 2)
- Crea una base de datos MySQL en Amazon RDS (`franquicia_db`)
- Inyecta dinámicamente el endpoint de RDS en el `docker-compose.yml`
- Clona el repositorio en EC2 y levanta la app automáticamente vía Docker

# Acceso:

- API disponible en: `http://<IP_PUBLICA>:8080/swagger-ui.html`
- Puedes ver la IP al final del despliegue o con:

```bash
  terraform output public_ip
```

---

# Conclusiones
- Este proyecto fue desarrollado siguiendo principios de Clean Architecture, promoviendo una estructura clara, desacoplada y preparada para escalar. Además, se aplicaron fundamentos de programación reactiva mediante Spring WebFlux, lo que permite una mayor eficiencia en el manejo de concurrencia.
# Estructura basada en Clean Architecture
- Entidades `entities`: Modelan el dominio principal `Franquicia`, `Sucursal`, `Producto`, sin depender de frameworks.
- DTOs `dto`: Aislados para representar entradas/salidas de la aplicación, protegiendo el core del dominio.
- Controladores `controller`: Manejan las solicitudes HTTP y responden con DTOs, sin lógica de negocio directa.
- Servicios `service`: Implementan la lógica del negocio orquestando la persistencia y transformaciones.
- Repositorios `repository`: Interfaces con JPA para el acceso a datos y persistencia de los mismos, desacoplados de la lógica de aplicación.

Esta estructura garantiza bajo acoplamiento, alta cohesión y facilidad para testeo, escalado y mantenimiento.

# Reactividad y elección tecnológica
- Se eligió Spring WebFlux por su capacidad de manejar múltiples conexiones simultáneas de manera eficiente y de forma no bloqueante `Mono`, `Flux`, ideal para aplicaciones con alta concurrencia y favoreciendo escalabilidad en microservicios.
- JPA con MySQL fue utilizado por su madurez, rapidez de desarrollo y compatibilidad. Aunque no es reactivo nativo, se estructuró el código para que la transición a un stack completamente no bloqueante sea viable.
- Los endpoints están diseñados para consumir y retornar `Mono<ResponseEntity<...>>`, permitiendo flujos reactivos desde el inicio.

# Buenas prácticas aplicadas
- Código limpio y legible: Métodos cortos, nombres expresivos y responsabilidades únicas.
- Separación clara de responsabilidades por capas.
- Control de errores centralizado y respuestas uniformes `ResponseDto` y uso de `@ResponseStatus` para manejar errores y devolver códigos HTTP adecuados..
- Uso de DTOs para evitar exponer entidades directamente.
- Pruebas unitarias que validan servicios clave `BranchService`, `ProductService`, `FranchiseService`.
- Variables de entorno y configuración externa para facilitar el cambio de entorno usando `application.yml`.
- Despliegue automatizado con Docker y Terraform.
- Uso de `.dockerignore`, `.gitignore` y buenas prácticas de Git para evitar archivos innecesarios en el control de versiones.

# Futuras mejoras
Para una arquitectura 100% reactiva y alineada con Clean Architecture en producción, se puede considerar:
- Implementar un cliente reactivo para `MySQL`, `PgSql` (ej. R2DBC) para aprovechar completamente la reactividad o `MongoDB` con `ReactiveMongoRepository`.
- Separación total del dominio del framework `pure domain entities`.

# Comentarios personales
- Este proyecto fue una excelente oportunidad para aplicar y profundizar en conceptos de arquitectura limpia, programación reactiva y despliegue automatizado. Incorporé una solución profesional, organizada y alineada con buenas prácticas modernas. Combina simplicidad, escalabilidad y apertura para evolución futura hacia una arquitectura más purista si el negocio lo requiere.

# Contacto
- [Correo](marlonvallejotst@gmail.com)