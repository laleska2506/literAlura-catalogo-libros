package com.alura.literAlura_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LiterAluraChallengeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LiterAluraChallengeApplication.class, args);
		
		// Obtener el MenuService y mostrar el menú
		MenuService menuService = context.getBean(MenuService.class);
		menuService.mostrarMenu();
		
		// Cerrar la aplicación después de salir del menú
		context.close();
	}

}
