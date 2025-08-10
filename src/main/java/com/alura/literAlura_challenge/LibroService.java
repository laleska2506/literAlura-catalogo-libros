package com.alura.literAlura_challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GUTENBERG_API_URL = "https://gutendex.com/books/";

    /**
     * Obtiene todos los libros disponibles desde la API de Gutenberg
     * y los guarda en la base de datos local
     */
    public List<LibroDTO> obtenerLibros() {
        try {
            ResponseEntity<GutenbergResponse> response = restTemplate.getForEntity(
                GUTENBERG_API_URL, GutenbergResponse.class);
            
            if (response.getBody() != null && response.getBody().results != null) {
                List<LibroDTO> libros = response.getBody().results.stream()
                    .map(this::convertirDesdeAPI)
                    .collect(Collectors.toList());
                
                // Guardar todos los libros en la BD
                guardarLibrosEnBD(response.getBody().results);
                
                return libros;
            }
        } catch (Exception e) {
            System.err.println("Error al obtener libros de la API: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }

    /**
     * Obtiene todos los libros registrados en la base de datos local
     * @return Lista de libros desde la BD
     */
    public List<LibroDTO> obtenerLibrosDesdeBD() {
        try {
            List<Libro> librosBD = libroRepository.findAll();
            return librosBD.stream()
                .map(LibroDTO::new)
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error al obtener libros desde la BD: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca libros por término de búsqueda en la API de Gutenberg
     * y los guarda en la base de datos local
     * @param searchTerm Término(s) de búsqueda (pueden ser múltiples separados por espacios)
     * @return Lista de libros que coinciden con la búsqueda
     */
    public List<LibroDTO> buscarLibros(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return obtenerLibros();
        }

        String searchUrl = construirUrlBusqueda(searchTerm);
        
        try {
            ResponseEntity<GutenbergResponse> response = restTemplate.getForEntity(
                searchUrl, GutenbergResponse.class);
            
            if (response.getBody() != null && response.getBody().results != null) {
                List<LibroDTO> libros = response.getBody().results.stream()
                    .map(this::convertirDesdeAPI)
                    .collect(Collectors.toList());
                
                // Guardar los libros encontrados en la BD
                guardarLibrosEnBD(response.getBody().results);
                
                return libros;
            }
        } catch (Exception e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }

    /**
     * Construye la URL de búsqueda para la API de Gutenberg
     */
    private String construirUrlBusqueda(String searchTerm) {
        String terminoLimpio = searchTerm.trim().replace(" ", "%20");
        return GUTENBERG_API_URL + "?search=" + terminoLimpio;
    }

    /**
     * Convierte un libro de la API de Gutenberg a nuestro DTO
     */
    private LibroDTO convertirDesdeAPI(GutenbergBook apiBook) {
        return new LibroDTO(
            apiBook.title != null ? apiBook.title : "Título desconocido",
            obtenerNombreAutor(apiBook.authors),
            obtenerFechaFallecimiento(apiBook.authors),
            obtenerFechaNacimiento(apiBook.authors),
            obtenerNumeroDescargas(apiBook.download_count),
            obtenerIdioma(apiBook.languages)
        );
    }

    /**
     * Guarda los libros de la API en la base de datos local
     */
    private void guardarLibrosEnBD(List<GutenbergBook> apiBooks) {
        for (GutenbergBook apiBook : apiBooks) {
            try {
                // Verificar si el libro ya existe en la BD
                if (!libroRepository.existsByTitulo(apiBook.title)) {
                    Libro libro = new Libro();
                    libro.setTitulo(apiBook.title);
                    libro.setAutor(obtenerNombreAutor(apiBook.authors));
                    libro.setFechaFallecimiento(obtenerFechaFallecimiento(apiBook.authors));
                    libro.setFechaNacimiento(obtenerFechaNacimiento(apiBook.authors));
                    libro.setNumeroDescargas(obtenerNumeroDescargas(apiBook.download_count));
                    libro.setIdioma(obtenerIdioma(apiBook.languages));
                    
                    libroRepository.save(libro);
                    System.out.println("Libro guardado en BD: " + apiBook.title);
                }
            } catch (Exception e) {
                System.err.println("Error al guardar libro en BD: " + apiBook.title + " - " + e.getMessage());
            }
        }
    }

    private String obtenerNombreAutor(List<GutenbergAuthor> authors) {
        return authors != null && !authors.isEmpty() 
            ? authors.get(0).name 
            : "Autor desconocido";
    }

    private String obtenerNumeroDescargas(Integer downloadCount) {
        return downloadCount != null 
            ? String.valueOf(downloadCount) 
            : "0";
    }

    private String obtenerIdioma(List<String> languages) {
        return languages != null && !languages.isEmpty() 
            ? languages.get(0) 
            : "en";
    }

    private String obtenerFechaNacimiento(List<GutenbergAuthor> authors) {
        if (authors != null && !authors.isEmpty() && authors.get(0).birth_year != null) {
            return String.valueOf(authors.get(0).birth_year);
        }
        return "Fecha de nacimiento no disponible";
    }

    private String obtenerFechaFallecimiento(List<GutenbergAuthor> authors) {
        if (authors != null && !authors.isEmpty() && authors.get(0).death_year != null) {
            return String.valueOf(authors.get(0).death_year);
        }
        return "Fecha de fallecimiento no disponible";
    }

    // ===== CLASES INTERNAS PARA MAPEAR LA RESPUESTA DE LA API =====

    /**
     * Respuesta principal de la API de Gutenberg
     */
    public static class GutenbergResponse {
        public List<GutenbergBook> results;
    }

    /**
     * Representa un libro individual de la API de Gutenberg
     * Solo incluye los campos que queremos guardar en la BD
     */
    public static class GutenbergBook {
        public String title;
        public List<GutenbergAuthor> authors;
        public Integer download_count;
        public List<String> languages;
    }

    /**
     * Representa un autor de la API de Gutenberg
     */
    public static class GutenbergAuthor {
        public String name;
        public Integer birth_year;
        public Integer death_year;
    }
}
