# 📚 LiterAlura Challenge

Una aplicación de consola desarrollada en **Java Spring Boot** que permite buscar y gestionar libros utilizando la **API de Gutenberg**. La aplicación integra búsquedas en tiempo real con persistencia local en base de datos PostgreSQL.

## 🚀 Características

- **🔍 Búsqueda en tiempo real** de libros por título/autor usando la API de Gutenberg
- **💾 Persistencia automática** de todos los libros buscados en base de datos local
- **📊 Gestión completa** de libros, autores e idiomas
- **🖥️ Interfaz de consola** intuitiva y fácil de usar
- **⚡ Respuestas rápidas** para consultas locales vs. búsquedas externas

## 🛠️ Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Hibernate ORM**
- **PostgreSQL**
- **Maven**
- **RestTemplate** para APIs externas

## 📋 Prerrequisitos

- **Java JDK 17** o superior
- **Maven 3.6+**
- **PostgreSQL 12+** ejecutándose localmente
- **Conexión a internet** para acceder a la API de Gutenberg

## ⚙️ Configuración

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd literAlura-challenge
```

### 2. Configurar base de datos PostgreSQL
```sql
-- Crear base de datos (opcional, se crea automáticamente)
CREATE DATABASE literAlura;
```

### 3. Configurar application.properties
Edita `src/main/resources/application.properties` con tus credenciales:

```properties
# Conexión a base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 4. Ejecutar la aplicación
```bash
mvn spring-boot:run
```

## 🎮 Uso de la Aplicación

### Menú Principal
```
=== LITERALURA - MENÚ PRINCIPAL ===
1. Buscar libro por título
2. Listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un determinado año
5. Listar libros por idioma
0. Salir
```

### Funcionalidades

#### 1. 🔍 Buscar libro por título
- Busca libros en la API de Gutenberg usando términos de búsqueda
- Los resultados se guardan automáticamente en la base de datos
- Soporta búsquedas con múltiples palabras separadas por espacios

#### 2. 📚 Listar libros registrados
- Muestra todos los libros almacenados en la base de datos local
- Incluye: título, autor, idioma, número de descargas, fechas del autor

#### 3. 👥 Listar autores registrados
- Lista todos los autores únicos de los libros almacenados
- Ordenados alfabéticamente

#### 4. 📅 Listar autores vivos en un año
- Filtra autores que estuvieron vivos en un año específico
- Basado en fechas de nacimiento y fallecimiento

#### 5. 🌍 Listar libros por idioma
- Filtra libros por código de idioma (es, en, fr, etc.)
- Búsqueda case-insensitive

## 🗄️ Estructura de la Base de Datos

### Tabla `libros`
```sql
CREATE TABLE libros (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(500) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    fecha_publicacion VARCHAR(100),
    fecha_fallecimiento VARCHAR(100),
    fecha_nacimiento VARCHAR(100),
    numero_descargas VARCHAR(50),
    idioma VARCHAR(10)
);

CREATE INDEX idx_libros_titulo ON libros(titulo);
CREATE INDEX idx_libros_autor ON libros(autor);
```

## 🏗️ Arquitectura del Proyecto

```
src/main/java/com/alura/literAlura_challenge/
├── LiterAluraChallengeApplication.java    # Punto de entrada principal
├── LibroController.java                   # Controlador REST (endpoints)
├── LibroService.java                      # Lógica de negocio y API externa
├── LibroRepository.java                   # Acceso a datos
├── Libro.java                             # Entidad JPA
├── LibroDTO.java                          # Objeto de transferencia
└── MenuService.java                       # Interfaz de consola
```

### Flujo de Datos
1. **Búsqueda**: Usuario → MenuService → LibroService → API Gutenberg
2. **Persistencia**: LibroService → LibroRepository → PostgreSQL
3. **Consulta Local**: MenuService → LibroService → LibroRepository → PostgreSQL

## 🔌 API Externa

### Gutenberg API
- **URL Base**: `https://gutendex.com/books/`
- **Endpoint de búsqueda**: `/books?search={término}`
- **Formato de respuesta**: JSON
- **Campos utilizados**: título, autor, idioma, descargas, fechas del autor

### Ejemplo de respuesta
```json
{
  "count": 76473,
  "results": [
    {
      "id": 2641,
      "title": "A Room with a View",
      "authors": [
        {
          "name": "Forster, E. M. (Edward Morgan)",
          "birth_year": 1879,
          "death_year": 1970
        }
      ],
      "languages": ["en"],
      "download_count": 121388
    }
  ]
}
```

## 🚨 Solución de Problemas

### Error de conexión a PostgreSQL
```
FATAL: la autentificación password falló para el usuario 'postgres'
```
**Solución**: Verifica las credenciales en `application.properties`

### Error de dialecto Hibernate
```
Unable to determine Dialect without JDBC metadata
```
**Solución**: Asegúrate de que PostgreSQL esté ejecutándose y accesible

### Libros no se guardan en BD
**Verificación**: Revisa los logs de consola para mensajes de error en `guardarLibrosEnBD`

## 📝 Notas de Desarrollo

- **Principio de Responsabilidad Única**: Cada clase tiene una responsabilidad específica
- **Separación de fuentes**: API externa para búsquedas, BD local para consultas
- **Manejo de errores**: Try-catch en operaciones críticas con logging apropiado
- **Validación de datos**: Verificación de existencia antes de guardar duplicados

## 🤝 Contribuciones

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Tu Nombre** - [tu-email@ejemplo.com](mailto:tu-email@ejemplo.com)

---

⭐ **¡Si te gustó el proyecto, dale una estrella!** ⭐
