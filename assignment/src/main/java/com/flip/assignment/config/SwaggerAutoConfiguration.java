package com.flip.assignment.config;

import com.flip.assignment.config.properties.SwaggerProperties;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({
        SwaggerProperties.class
})
@PropertySource(
        ignoreResourceNotFound = true,
        value = "classpath:application.yml"
)
public class SwaggerAutoConfiguration {

    @Bean
    public OpenAPI ngHotelSolutionAdminApi(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(new Info().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion()))
                .externalDocs(new ExternalDocumentation()
                        .description(swaggerProperties.getExternalDocsDescription())
                        .url(swaggerProperties.getExternalDocsUrl()))
                ;
    }
}
