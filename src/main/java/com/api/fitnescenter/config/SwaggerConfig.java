    package com.api.fitnescenter.config;

    import io.swagger.v3.oas.models.OpenAPI;
    import io.swagger.v3.oas.models.info.Info;
    import io.swagger.v3.oas.models.security.SecurityRequirement;
    import io.swagger.v3.oas.models.security.SecurityScheme;
    import io.swagger.v3.oas.models.servers.Server;
    import org.springdoc.core.configuration.SpringDocConfiguration;
    import org.springdoc.core.utils.SpringDocUtils;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.context.annotation.Import;

    @Configuration
    @Import(SpringDocConfiguration.class)
    @EnableWebSecurity
    public class SwaggerConfig {

        static {
            SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class, org.springframework.data.domain.Pageable.class);
            SpringDocUtils.getConfig().addRequestWrapperToIgnore(RequestMethod.class);
        }

        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .info(new Info()
                            .title("Fitness Center API")
                            .version("1.0.0")
                            .description("API documentation for Fitness Center"))
                    .addServersItem(new Server().url("/"))
                    .components(new io.swagger.v3.oas.models.Components()
                            .addSecuritySchemes("bearer-key", new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
        }
    }