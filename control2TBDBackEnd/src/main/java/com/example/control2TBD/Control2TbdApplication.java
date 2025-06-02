package com.example.control2TBD;

import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
