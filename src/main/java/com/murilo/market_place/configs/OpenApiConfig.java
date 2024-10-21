package com.murilo.market_place.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "Market Place Documentation",
                version = "1.0",
                termsOfService = "Terms of Service example",
                contact = @Contact(
                        name = "Murilo Vieira Cruz",
                        email = "murilo12super@gmail.com",
                        url = "https://murilocoding.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "https://licence-url.com"
                )
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "Test Environment",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}
