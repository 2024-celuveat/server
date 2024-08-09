package com.celuveat.common.adapter.`in`.rest

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${swagger.server-url}")
    private val serverUrl: String,
) {
    @Bean
    fun openAPI(): OpenAPI {
        val jwt = "JWT"
        val securityRequirement: SecurityRequirement = SecurityRequirement()
            .addList(jwt)
        val components = Components()
            .addSecuritySchemes(
                jwt,
                SecurityScheme()
                    .name(jwt)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .description("토큰값을 입력하여 인증을 활성화할 수 있습니다.")
                    .bearerFormat("JWT"),
            )
        val server: Server = Server()
        server.setUrl(serverUrl)
        return OpenAPI()
            .components(Components())
            .info(apiInfo())
            .addSecurityItem(securityRequirement)
            .components(components)
            .addServersItem(server)
    }

    private fun apiInfo(): Info {
        return Info()
            .title("Celuveat API")
            .description("Celuveat API 문서입니다.")
            .version("1.0")
    }
}
