package com.example.demo.infra.security.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // todos os endpoints e requisições da aplicação
                .allowedOrigins("http://localhost:4200") // porta do frontend, se for fazer o deploy, mudar aqui onde está hospedado
                .allowedMethods("GET", "POST","DELETE", "PUT"); // métodos permitidos
    }
}
