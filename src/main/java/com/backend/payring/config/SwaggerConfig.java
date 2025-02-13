package com.backend.payring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration  // Spring 설정 클래스로 등록
public class SwaggerConfig {


    @Bean
    public OpenAPI PAYRING_API() {

        Server testServer = new Server();
        testServer.setUrl("http://localhost:8080/");

        Server deployServer = new Server();
        deployServer.setUrl("https://storyteller-backend.site/");


        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .description("토큰값을 입력하여 인증을 활성화할 수 있습니다.")
                .bearerFormat("JWT")
        );

        Info info = new Info()
                .title("PAYRING API")
                .description("PAYRING API 명세서")
                .version("1.0.0");


        return new OpenAPI()
                .components(components)
                .info(info)
                .addSecurityItem(securityRequirement)
                .servers(List.of(deployServer, testServer));
    }
}
