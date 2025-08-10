package com.alura.literAlura_challenge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private String fechaFallecimiento;
    private String fechaNacimiento;
    private String numeroDescargas;
    private String idioma;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getNumeroDescargas() {
        return numeroDescargas;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setNumeroDescargas(String numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setFechaFallecimiento(String fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

}
