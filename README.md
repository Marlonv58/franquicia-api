# Franquicia API

API REST construida con **Spring Boot + WebFlux**, para gestionar franquicias, sucursales y productos.  
Esta solución incluye programación reactiva, validaciones automáticas, pruebas unitarias, despliegue automatizado con **Docker**, **Terraform** y ejecución en **AWS EC2**.

---

# Tecnologías utilizadas

- Java 17
- Spring Boot 3.5.0 + WebFlux
- MySQL 8
- Jakarta validation
- Docker + Docker Compose
- Terraform + AWS EC2 + Amazon RDS
- JUnit + Mockito
- Maven Wrapper
- Actuator

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

# Docuemntación y pruebas
Swagger/OpenAPI fue removido por conflictos de compatibilidad entre versiones de springdoc y Spring Boot 3.5.x.
Para garantizar compatibilidad y estabilidad del despliegue, se optó por deshabilitar Swagger.
Para probar los endpoints, se recomienda usar herramientas como Postman o Insomnia.
para verificar si el backend está funcionando, puedes hacer una petición GET a `http://localhost:8080/actuator/health`, que debería devolver un estado `UP`.

# Como probar los endpoints:
Se incluye una colección de Postman (franquicia-api.postman_collection.json) con todos los endpoints listos para probar:
- Crear franquicia.
- Crear sucursal.
- Crear producto.
- Actualizar nombre de sucursal.
- Actualizar nombre de franquicia.
- Actualizar nombre de producto.
- Actualizar stock de producto.
- Consultar producto con mas stock por sucursal.
- Eliminar producto.
- Consultar si el backend está funcionando.

# Validaciones
Se implementaron validaciones automáticas con `@Valid` y `@Validated` en los DTOs, asegurando que los datos de entrada cumplan con las reglas definidas:
```java
@NotBlank(message = "El nombre de la franquicia no puede estar vacío")
private String name;
```
# Ejemplo de validación fallida
``` json
{
  "status": false,
  "detail": "name: El nombre de la franquicia no puede estar vacío",
  "response": null
}
```

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

---

# Ejecutar en entorno productivo (AWS con Terraform)

El entorno de producción se levanta automáticamente en una instancia EC2 en AWS y se conecta con una base de datos MySQL en RDS.

# Requisitos:

- Tener configurado el CLI de AWS con un usuario que pueda craer ec2, security groups vpc, rds y otras cosas, se podria usar un addminAcces para la prueba o despleigue:
```bash
  aws configure
```
- Este proyecto no requiere que el archivo `.pem` exista localmente para poder desplegar con Terraform.
- Toda la instalación del backend y configuración de Docker se realiza automáticamente vía user_data (un script que corre dentro de la instancia EC2 al arrancar).
- En la consola de AWS, ve a EC2 → Key Pairs, y crea un nuevo par de claves llamado:
```vbnet
  franquicia-key
```
si quieres usar otra Key pars deberas modificar el nombre de main.tf en esta seccion para que coincida y pueda levantar una instancia ec2
``` hcl
  variable "key_name" {
  description = "Name of SSH key in AWS EC2"
  default     = "franquicia-key" <------- nombre a cambiar
}
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

- utiliza el output de la ip con el endpoint de consultar el estado de la aplicación que pasé en la collection de Postman:
```
  http://{{IP}}:8080/actuator/health
```
cuando terraform despliegue la aplicación, te mostrará la IP pública de la instancia EC2 que debes poner en la petición.
```
public_ip = "ip pública de la instancia EC2"
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
