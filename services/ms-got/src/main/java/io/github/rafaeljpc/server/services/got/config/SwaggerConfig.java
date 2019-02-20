package io.github.rafaeljpc.server.services.got.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.rafaeljpc.server.services"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .termsOfServiceUrl("Terms of Service")
                .title("GoT API Test Application")
                .description("Game of Thrones API")
                .license("Apache License 2.0")
                .version("0.1")
                .contact(new Contact(
                        "Rafael Correia",
                        "https://rafaeljpc.github.io/",
                        "rafaeljpc@gmail.com"
                ))
                .build();
    }
}
