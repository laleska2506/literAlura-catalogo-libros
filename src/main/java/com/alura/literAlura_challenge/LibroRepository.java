package com.alura.literAlura_challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Libro> buscarPorTituloOAutor(@Param("searchTerm") String searchTerm);
    
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
    List<Libro> findByAutorContainingIgnoreCase(String autor);
    
    // Verificar si existe un libro por título
    boolean existsByTitulo(String titulo);
}
