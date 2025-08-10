package com.alura.literAlura_challenge;

public record LibroDTO(
    String titulo,
    String autor,
    String fechaFallecimiento,
    String fechaNacimiento,
    String numeroDescargas,
    String idioma
    ) {
    public LibroDTO(Libro libro) {
        this(libro.getTitulo(), libro.getAutor(), libro.getFechaFallecimiento(), libro.getFechaNacimiento(), libro.getNumeroDescargas(), libro.getIdioma());
    }
}