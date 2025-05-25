# Franchise API - Prueba Técnica Backend

Esta es una API desarrollada como parte de una prueba técnica backend. Permite manejar franquicias, sucursales y productos. Está construida con **Spring Boot**, **programación reactiva (WebFlux)**, **JPA con MySQL**, y empaquetada con **Docker**. Se puede desplegar en AWS usando **Terraform**.

---

## Requisitos

- Java 17+
- Maven 3.8+
- Docker
- Docker Compose
- Cuenta AWS (para despliegue opcional)

---

## Tecnologías utilizadas

- Spring Boot 3.5.0
- Spring WebFlux (programación reactiva)
- Spring Data JPA + Hibernate
- MySQL 8
- Docker + Docker Compose
- Terraform (provisionamiento en AWS)
- Springdoc OpenAPI (Swagger)
- Lombok

---

## ⚙️ Estructura del Proyecto

```
franquicia-api/
├── src/
│   ├── main/java/com/ejemplo/franquicia/
│   │   ├── controller/         # Controladores con WebFlux
│   │   ├── service/            # Lógica de negocio
│   │   ├── repository/         # Interfaces JPA
│   │   ├── dto/                # Objetos de entrada/salida
│   │   ├── model/              # Entidades JPA
│   ├── resources/
│       └── application.yml     # Configuración de base de datos
├── Dockerfile
├── docker-compose.yml
├── terraform/
│   └── main.tf                 # Infraestructura con Terraform
└── README.md
```

---

## 📦 Cómo levantar localmente

### 1. Clonar el repositorio
```bash
git clone https://github.com/tuusuario/franquicia-api.git
cd franquicia-api
```

### 2. Construir la app
```bash
mvn clean package
```

### 3. Levantar con Docker Compose
```bash
docker-compose up --build
```

La API estará disponible en: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🌐 Endpoints principales

| Recurso   | Método | Ruta                                          | Descripción                                     |
|-----------|--------|-----------------------------------------------|-------------------------------------------------|
| Franquicia| POST   | /api/franchises/create                       | Crea una nueva franquicia                       |
| Franquicia| PATCH  | /api/franchises/{id}                         | Actualiza el nombre de una franquicia           |
| Sucursal  | POST   | /api/branches/add                            | Agrega una sucursal a una franquicia            |
| Sucursal  | PATCH  | /api/branches/{id}                           | Actualiza el nombre de una sucursal             |
| Producto  | POST   | /api/products/add                            | Agrega un producto a una sucursal               |
| Producto  | PATCH  | /api/products/{productId}/stock              | Modifica el stock de un producto                |
| Producto  | DELETE | /api/products/{productId}                    | Elimina un producto de una sucursal             |
| Reporte   | GET    | /api/franchises/{id}/max-stock-products      | Lista productos con más stock por sucursal      |

---

## 🧠 ¿Por qué WebFlux con MySQL?

Aunque JPA y MySQL son bloqueantes, se usó **Spring WebFlux** con controladores reactivos (`Mono<ResponseEntity<?>>`) para cumplir el criterio de programación reactiva. Se adaptó el acceso a datos bloqueantes usando `Mono.fromCallable(...)`, una práctica válida en migraciones hacia aplicaciones reactivas.

Esto permite que la capa HTTP funcione de forma no bloqueante sobre Netty, y sea escalable y eficiente en concurrencia.

---

## ☁️ Despliegue en AWS con Terraform (opcional)

1. Crea un par de llaves en AWS EC2.
2. Edita `terraform/main.tf` con tu nombre de llave (`key_name`).
3. Ejecuta desde la carpeta `terraform`:
```bash
terraform init
terraform apply
```
4. SSH al servidor y ejecuta:
```bash
sudo docker-compose up --build -d
```

---

## ✅ Criterios de aceptación cumplidos

- [x] Crear franquicia
- [x] Agregar sucursal
- [x] Agregar producto a sucursal
- [x] Eliminar producto
- [x] Modificar stock
- [x] Obtener productos con más stock por sucursal
- [x] Programación reactiva con WebFlux
- [x] Docker y Docker Compose
- [x] Actualización de nombres (franquicia, sucursal, producto)
- [x] Infraestructura como código con Terraform
- [x] Despliegue en nube (EC2 con Docker)

---

## 📫 Contacto
Cualquier duda, puedes contactar a: **[tu nombre o correo]**
