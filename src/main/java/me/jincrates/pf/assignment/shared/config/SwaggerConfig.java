package me.jincrates.pf.assignment.shared.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "API Server")})
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(info());
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("0. 전체 API")
            .pathsToMatch("/api/**")
            .pathsToExclude("/api/categories/**") // 카테고리 API 제외
            .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
            .group("1. 상품 API")
            .pathsToMatch("/api/products/**")
            .build();
    }

    @Bean
    public GroupedOpenApi reviewApi() {
        return GroupedOpenApi.builder()
            .group("2. 리뷰 API")
            .pathsToMatch("/api/reviews/**")
            .build();
    }

    private Info info() {
        Contact contact = new Contact()
            .name("이진규")
            .email("jg.lee@pet-friends.co.kr")
            .url("https://github.com/jincrates-lee");

        return new Info()
            .title("petfriends-backend-api")
            .description("펫프렌즈 백엔드 과제")
            .contact(contact)
            .version("1.0");
    }
}
