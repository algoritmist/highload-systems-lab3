package itmo.highload.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag

@OpenAPIDefinition(
    info = Info(
        title = "Place Service API",
        version = "1.0.0",
        description = "API documentation for managing places in the application.",
        contact = Contact(
            name = "Support Team",
            email = "support@example.com",
            url = "https://example.com"
        ),
        license = License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = [
        Server(
            url = "http://localhost:8080",
            description = "Local environment"
        ),
        Server(
            url = "https://api.example.com",
            description = "Production environment"
        )
    ]
)
class OpenApiConfig