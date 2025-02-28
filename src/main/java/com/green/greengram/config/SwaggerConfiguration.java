package com.green.greengram.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "${constants.swagger.info.title}"
        , description = "${constants.swagger.info.description}"
        , version = "${constants.swagger.info.version}"
    )
    , security = @SecurityRequirement(name = "${constants.swagger.authorization.name}")
)

@SecurityScheme(
    type = SecuritySchemeType.HTTP
    , name = "${constants.swagger.authorization.name}"
    , in = SecuritySchemeIn.HEADER
    , bearerFormat = "${constants.swagger.authorization.bearer-format}"
    , scheme = "${constants.swagger.authorization.scheme}"
)
@Configuration
public class SwaggerConfiguration {
    @Bean
    public GroupedOpenApi groupGreengramApi() {
        return GroupedOpenApi.builder()
                .group("Greengram")
                .pathsToMatch("/api/feed/**", "/api/user", "/api/user/pic", "/api/product/**")
                .build();
    }

    @Bean
    public GroupedOpenApi groupAuthApi() {
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/api/user/sign-up", "/api/user/sign-in", "/api/user/access-token")
                .build();
    }

    @Bean
    public GroupedOpenApi groupPayApi() {
        return GroupedOpenApi.builder()
                .group("Pay")
                .packagesToScan("com.green.greengram.kakaopay")
                .build();
    }

    @Bean
    public GroupedOpenApi groupCommonApi() {
        return GroupedOpenApi.builder()
                .group("Common")
                .pathsToMatch("/api/common/**")
                .build();
    }
}
