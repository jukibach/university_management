package fpt.com.universitymanagement.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.core.env.Environment;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
    private final Environment environment;
    
    public SwaggerConfig(Environment environment) {
        this.environment = environment;
    }
    
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(environment.getProperty("swagger.title"))
                        .description(environment.getProperty("swagger.description"))
                        .version(environment.getProperty("swagger.version"))
                        .license(new License()
                                .name(environment.getProperty("swagger.license"))
                                .url(environment.getProperty("swagger.licenseUrl"))));
    }
}

