# Pizza Shop

API REST de una pizzería hecha con **Spring Boot** para practicar **Spring Data JPA** y **Spring Security con JWT**: consultas derivadas, `@Query` nativas, paginación, proyecciones, auditoría de entidades y control de acceso por roles.

## Stack

- Java 17 y Spring Boot 3.2 (Gradle)
- Spring Data JPA + MySQL
- Spring Security + JWT (`java-jwt`)
- Swagger / OpenAPI (`springdoc`)
- Lombok
- JUnit 5 (H2 en memoria para los tests)

## Cómo ejecutar

1. Levantar MySQL. La primera vez crea las tablas y carga los datos de ejemplo (`scriptDB.sql` y `ScriptInsertData.sql`):
   ```bash
   docker compose up -d
   ```

2. Definir el secreto con el que se firman los tokens. Es obligatorio y no tiene valor por defecto:
   ```bash
   export JWT_SECRET="un-secreto-largo-y-aleatorio"
   ```

3. Arrancar la aplicación:
   ```bash
   ./gradlew bootRun
   ```

La API queda en `http://localhost:8080/pizzaShopServices` y Swagger en `http://localhost:8080/pizzaShopServices/api/public/swagger-ui.html`.

## Variables de entorno

| Variable | Valor por defecto | Para qué sirve |
|---|---|---|
| `JWT_SECRET` | — (obligatoria) | Secreto para firmar y validar los JWT. |
| `DB_URL` | `jdbc:mysql://localhost:3306/pizza_shop` | URL de la base de datos. |
| `DB_USERNAME` | `admin_pizza_shop` | Usuario de la base. |
| `DB_PASSWORD` | `admin_pizza_shop` | Contraseña de la base. |
| `CORS_ALLOWED_ORIGINS` | `*` | Orígenes permitidos, separados por comas. |
| `MYSQL_ROOT_PASSWORD` (solo compose) | `admin` | Contraseña de root del contenedor. |

Los valores por defecto de la base coinciden con `docker-compose.yml` y son solo para desarrollo local.

## Autenticación

`POST /api/auth/login` con `{"username": "...", "password": "..."}` devuelve el token en la cabecera `Authorization`. Las demás peticiones lo envían como `Authorization: Bearer <token>`.

Los usuarios y sus roles están en las tablas `user` y `user_role`. Un usuario bloqueado o deshabilitado no puede usar la API aunque su token siga vigente.

## Endpoints

| Ruta | Método | Acceso |
|---|---|---|
| `/api/auth/login` | `POST` | Público |
| `/api/pizzas/**` | `GET` | `ADMIN` o `CUSTOMER` |
| `/api/pizzas/save` | `POST` | `ADMIN` |
| `/api/pizzas/update`, `/api/pizzas/price` | `PUT` | `ADMIN` |
| `/api/pizzas/{idPizza}` | `DELETE` | `ADMIN` |
| `/api/customers/**` | `GET` | `ADMIN` o `CUSTOMER` |
| `/api/orders/random` | `POST` | Permiso `random_order` |
| `/api/orders/**` | `GET` | `ADMIN` |

`/api/orders/random` llama al procedimiento almacenado `take_random_pizza_order`, que no está incluido en los scripts de este repositorio.

## Tests

```bash
./gradlew test
```

No necesitan MySQL ni Docker: usan H2 en memoria. Cubren la creación y validación de tokens, el acceso por roles, los usuarios bloqueados, el login y la configuración de CORS.
