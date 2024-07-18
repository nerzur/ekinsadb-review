package cu.havanaclub.ekinsadbreview.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "EKINSA server-db errors detector.",
        version = "1.0",
        description = "This page contains the different api-rest services available in this project. " +
                "In the same way, examples and the possibility of making test requests are shown.",
        contact = @Contact(name = "GABRIEL A. PEREZ GUERRA",
                email = "gabrielpga20@gmail.com")
))
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@EnableScheduling
public class SpringConfig {
}
