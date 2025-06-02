package com.example.control2TBD;

import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// Ejecutar con el comando:
// .\mvnw spring-boot:run
// para cargar correctamente la confguración de maven, independientemente del IDE.

@SpringBootApplication
public class Control2TbdApplication {

	public static void main(String[] args) {
        SpringApplication.run(Control2TbdApplication.class, args);
    }

    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}
