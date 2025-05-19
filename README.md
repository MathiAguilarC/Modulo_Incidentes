# 🛠️ Backend de Gestión de Reportes e Incidencias

Este proyecto implementa una API REST en **Spring Boot** para la gestión de reportes, incidencias y soluciones en un sistema de soporte técnico. El backend permite interacción según el tipo de usuario: **cliente**, **empleado soporte** y **administrador**.

---

## 🚀 Tecnologías usadas

- Java 17+
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Maven

---

## 🗃️ Base de datos

Nombre: `tiendaSoporte`  
Motor: **PostgreSQL**

Tablas:
- `Cliente`
- `EmpleadoSoporte`
- `Reporte`
- `Incidencia`
- `Soluciones`

📌 Se deben insertar usuarios con contraseñas encriptadas usando BCrypt.

---

## 🧪 Endpoints principales

### 🔐 Autenticación

| Método | Endpoint             | Descripción            |
|--------|----------------------|------------------------|
| POST   | `/api/auth/login`    | Login con JWT          |

---

### 👤 Cliente

| Método | Endpoint                | Descripción                          |
|--------|-------------------------|--------------------------------------|
| POST   | `/cliente/generar`      | Generar un reporte                   |
| GET    | `/cliente/reportes`     | Ver reportes enviados                |

---

### 🧑‍🔧 Soporte

| Método | Endpoint                 | Descripción                                 |
|--------|--------------------------|---------------------------------------------|
| GET    | `/soporte/reportes`      | Ver reportes asignados                      |
| PUT    | `/soporte/incidencia`    | Actualizar estado y comentario de incidencia |
| GET    | `/soporte/incidencia/{id}` | Ver detalles de incidencia asignada         |

---

### 👨‍💼 Administrador

| Método | Endpoint                        | Descripción                                      |
|--------|----------------------------------|--------------------------------------------------|
| GET    | `/admin/incidencias/pendientes` | Ver incidencias sin asignar o pendientes         |
| POST   | `/admin/incidencias/asignar`    | Asignar incidencia a soporte y marcar "En Progreso" |
| GET    | `/admin/incidencia/{id}`        | Ver detalles de cualquier incidencia             |

---

## 🔐 Seguridad

- Autenticación vía **JWT**
- Cada solicitud protegida debe incluir:

## Authorization: Bearer <token>
- Los usuarios deben autenticarse por correo y contraseña.
- Los endpoints están protegidos según rol (`cliente`, `soporte`, `administrador`).

---

# 🛠️ Cómo ejecutar el proyecto

1. Clona este repositorio:

```bash
git clone https://github.com/MathiAguilarC/Modulo_Incidentes.git
cd nombre_repositorio
```
2. Configura application.properties o application.yml con tu conexión PostgreSQL:
 ```  
spring.datasource.url=jdbc:postgresql://localhost:5432/tiendaSoporte
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password


spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

jwt.secret=clave_secreta_segura
jwt.expiration=3600000

```
3. Ejecuta la aplicación desde tu IDE o usando:
```   
./mvnw spring-boot:run
```

## 📂 Estructura del proyecto

```
├── controller/
├── service/
├── repository/
├── model/
├── dto/
└── security/
```

## 📮 Postman
Puedes probar todos los endpoints usando Postman o cualquier cliente HTTP.
Asegúrate de iniciar sesión y enviar el JWT en cada solicitud protegida.
## 🧑‍💻 Autor(a)
Este backend fue desarrollado para integrarse fácilmente con un frontend independiente.
