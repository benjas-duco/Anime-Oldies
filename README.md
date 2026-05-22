# AnimeOldies API

API REST construida con Spring Boot para gestionar una base de datos de animes antiguos, donde se pueden crear usuarios con los que luego se pueden publicar animes y reseñas. Con sistema de categorías, links externos y cálculo de "Cult Level" basado en las reseñas del anime.

## Tecnologías usadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- PostgreSQL
- SupaBase

## Características Principales

- CRUD completo de Animes, Reviews y Usuarios
- Sistema de categorías dinámicas
- Links asociados a cada anime
- Cálculo de "Cult Level" basado en reviews aprobadas
- Estados de anime (en revision, aprobado, rechazado)
- Estados de reseñas (en revision, aprobado, rechazado)
- Relaciones Many-To-One
- Validaciones
- Uso de DTOs
- Manejos de excepciones
- Respuestas con ResponseEntity
- Conexión a base de datos remota (SupaBase)

## Estructura

- controller/ -> Endpoints REST
- service/ -> Logica de negocio
- repository/ -> Acceso a la base de datos
- model/ -> Entidades JPA
- DTOs/ -> Objetos de transferencia de datos
- exception/ -> Entidad de manejo de excepciones

## Endpoints Principales (no todos)

### Anime

- POST /api/v1/anime -> Crear anime
- GET /api/v1/anime -> Obtener lista animes
- GET /api/v1/anime/{id} -> Obtener anime por ID
- PUT /api/v1/anime/{id} -> Actualizar anime
- DELETE /api/v1/anime/{id} -> Eliminar anime

### Reviews

- POST /api/v1/reviews -> Crear review
- PUT /api/v1/reviews/{id} -> Actualizar review
- DELETE /api/v1/reviews/{id} -> Eliminar review

### Consultas

- GET /api/v1/anime/{id}/state -> Estado del anime
- GET /api/v1/anime/{id}/reviews?state=aprobado -> Reviews aprobadas
- GET /api/v1/anime/by-category/{id} -> Animes por categoría
- GET /api/v1/anime/by-state/{estado} -> Animes por estado
- GET /api/v1/anime/{id}/links -> Los links del anime

## Logica de negocio (Cult Level)

Decidí implementar mi propia lógica de negocio, donde se toman la cantidad de reviews y sus scores para calcular un nivel de culto, osea que, que tan de nicho pero bueno es el anime. Si tiene pocas reviews y muy buenas es muy de culto, pero si tiene muchas reviews, aunque sean todas muy buenas, deja de ser de culto. Esto se calcula con un algoritmo donde solo se cuentan las reviews aprobadas.

## Notas importantes

#### - El sistema asume que los "estados" existen previamente en la base de datos (en SupaBase)
#### - Los nombres de los estados deben coincidir exactamente: "en revision", "aprobado", "rechazado". De otra manera muchas de las consultas que utilizan estados no se realizaran correctamente.

## Autores

El proyecto fue hecho como desarrollo evaluativo de conocimientos del framework Spring Boot y del funcionamiento de las API. Este proyecto fue hecho en solitario.
