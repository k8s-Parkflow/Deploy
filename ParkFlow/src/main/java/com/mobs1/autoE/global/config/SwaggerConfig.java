package com.mobs1.autoE.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mobs_API() {
        Info info = new Info()
                .title("글다 API")
                .version("1.0.0")
                .description("autoEver mobsw REST API 문서입니다.");


        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .components(new Components())
                .info(info);

    }
}
