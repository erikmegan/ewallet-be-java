package com.flip.assignment.config.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String title;
    private String description;
    private String version;
    private String externalDocsDescription;
    private String externalDocsUrl;
}
