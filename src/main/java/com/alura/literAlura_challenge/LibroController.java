package com.alura.literAlura_challenge;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/books")
public class LibroController {
    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<LibroDTO> obtenerLibros(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return libroService.buscarLibros(search);
        }
        return libroService.obtenerLibros();
    }
}
