package com.lenaevd.calendar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI calendarOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Calendar API")
                        .description("""
                                Сервис григорианского календаря (после 1600 года),\s
                                использующий шаблоны для годов с одинаковой раскладкой\s
                                (по первому дню недели года и високосности)""")
                        .version("2.0.0"));
    }
}
