package ru.test.numberservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger для API сервиса.
 * Предоставляет документацию OpenAPI с описанием эндпоинтов.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Number Service API")
                        .version("1.0")
                        .description("A service for finding the Nth minimum number in Excel files")
        );
    }
}
