package com.example.canya.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    //Docket class는 Swagger 설정을 도와주는 클래스.
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext())) // jwt
                .securitySchemes(Arrays.asList(apiKey())); // jwt
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Ca-Nya☕")
                .description("카페 리뷰 웹사이트. 카페 좋아하냐?")
                .version("version 1")
                .contact(new Contact("카냐", "홈페이지 URL", "e-mail"))
                .build();
    }

    //Api key 만들어주기
    private ApiKey apiKey() {
        return new ApiKey("jwt", "Authorization", "header");
    }

    //jwt SecurityContext
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    //전역으로 jwt 적용.
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEveryThing");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("jwt", authorizationScopes));
    }
}
