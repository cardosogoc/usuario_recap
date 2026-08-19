package com.javanauta.usuario_recap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsuarioRecapApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioRecapApplication.class, args);
	}

}
