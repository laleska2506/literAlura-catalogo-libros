package com.alura.literAlura_challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private LibroService libroService;
    
    private Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== LITERALURA - MENÚ PRINCIPAL ===");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros registrados");
            System.out.println("3. Listar autores registrados");
            System.out.println("4. Listar autores vivos en un determinado año");
            System.out.println("5. Listar libros por idioma");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
                opcion = -1;
            }
        } while (opcion != 0);
        
        System.out.println("¡Hasta luego!");
        scanner.close();
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                buscarLibroPorTitulo();
                break;
            case 2:
                listarLibrosRegistrados();
                break;
            case 3:
                listarAutoresRegistrados();
                break;
            case 4:
                listarAutoresVivosEnAno();
                break;
            case 5:
                listarLibrosPorIdioma();
                break;
            case 0:
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida. Por favor intente de nuevo.");
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("\n--- BUSCAR LIBRO POR TÍTULO ---");
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        
        List<LibroDTO> libros = libroService.buscarLibros(titulo);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros con ese título.");
        } else {
            System.out.println("Libros encontrados:");
            libros.forEach(libro -> {
                System.out.println("------- LIBRO -------");
                System.out.println("Título: " + libro.titulo());
                System.out.println("Autor: " + libro.autor());
                System.out.println("Idioma: " + libro.idioma());
                System.out.println("Descargas: " + libro.numeroDescargas());
                System.out.println("---------------------");
            });
        }
    }

    private void listarLibrosRegistrados() {
        System.out.println("\n--- LIBROS REGISTRADOS ---");
        List<LibroDTO> libros = libroService.obtenerLibrosDesdeBD();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en el sistema.");
        } else {
            libros.forEach(libro -> {
                System.out.println("Título: " + libro.titulo());
                System.out.println("Autor: " + libro.autor());
                System.out.println("Idioma: " + libro.idioma());
                System.out.println("Descargas: " + libro.numeroDescargas());
                System.out.println("---");
            });
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n--- AUTORES REGISTRADOS ---");
        List<LibroDTO> libros = libroService.obtenerLibrosDesdeBD();
        if (libros.isEmpty()) {
            System.out.println("No hay autores registrados en el sistema.");
        } else {
            List<String> autores = libros.stream()
                    .map(LibroDTO::autor)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            
            autores.forEach(autor -> {
                System.out.println("Autor: " + autor);
                System.out.println("---");
            });
        }
    }

    private void listarAutoresVivosEnAno() {
        System.out.println("\n--- AUTORES VIVOS EN UN AÑO ---");
        System.out.print("Ingrese el año: ");
        try {
            int ano = Integer.parseInt(scanner.nextLine());
            List<LibroDTO> libros = libroService.obtenerLibrosDesdeBD();
            
            List<String> autores = libros.stream()
                    .filter(libro -> {
                        try {
                            int anoNacimiento = Integer.parseInt(libro.fechaNacimiento());
                            int anoFallecimiento = Integer.parseInt(libro.fechaFallecimiento());
                            return anoNacimiento <= ano && anoFallecimiento >= ano;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    })
                    .map(LibroDTO::autor)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores que esten vivos en " + ano);
            } else {
                System.out.println("Autores que esten vivos en " + ano + ":");
                autores.forEach(autor -> {
                    // Find a book by this author to get their dates
                    LibroDTO libroAutor = libros.stream()
                            .filter(libro -> libro.autor().equals(autor))
                            .findFirst()
                            .orElse(null);
                    
                    if (libroAutor != null) {
                        System.out.println("--------------------------------");
                        System.out.println("Autor: " + autor);
                        System.out.println("Fecha de nacimiento: " + libroAutor.fechaNacimiento());
                        System.out.println("Fecha de fallecimiento: " + libroAutor.fechaFallecimiento());
                        System.out.println("Libros publicados: " + libroAutor.titulo());
                        System.out.println("--------------------------------");
                    }
                });
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor ingrese un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n--- LIBROS POR IDIOMA ---");
        System.out.print("Ingrese el idioma (es, en, fr, etc.): ");
        String idioma = scanner.nextLine().toLowerCase();
        
        List<LibroDTO> libros = libroService.obtenerLibrosDesdeBD();
        List<LibroDTO> librosPorIdioma = libros.stream()
                .filter(libro -> libro.idioma().toLowerCase().contains(idioma))
                .collect(Collectors.toList());
        
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + idioma);
        } else {
            System.out.println("Libros en " + idioma + ":");
            librosPorIdioma.forEach(libro -> {
                System.out.println("--------------------------------");
                System.out.println("Título: " + libro.titulo());
                System.out.println("Autor: " + libro.autor());
                System.out.println("Idioma: " + libro.idioma());
                System.out.println("Descargas: " + libro.numeroDescargas());
                System.out.println("---");
            });
        }
    }
}
