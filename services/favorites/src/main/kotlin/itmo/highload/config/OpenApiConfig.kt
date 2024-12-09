package itmo.highload.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag

@OpenAPIDefinition(
    info = Info(
        title = "Favorites Service API",
        version = "1.0.0",
        description = "API documentation for favorites service",
        contact = Contact(
            name = "Vyacheslav",
            email = "335185@niuitmo.ru",
            url = "https://github.com/algoritmist"
        ),
    ),
    servers = [
        Server(
            url = "http://localhost:\${server.port}",
            description = "Local environment"
        )
    ]
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class OpenApiConfig