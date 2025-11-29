package com.zentry.sigea.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:16001}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
            .info(new Info()
                    .title("SIGEA API")
                    .description("Sistema Integral de Gestión de Eventos Académicos - API REST")
                    .version("v1.0.0")
                    .contact(new Contact()
                        .name("Equipo SIGEA")
                        .email("sigea@zentry.com")
                        .url("https://github.com/PAULTB4/SIGEA-backend.git"))
                    .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Servidor de desarrollo"),
                new Server()
                    .url("https://api.sigea.com")
                    .description("Servidor de producción")
            ))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                        .addSecuritySchemes("administradorJWT", 
                            new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes("organizadorJWT", 
                            new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes("participanteJWT", 
                            new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
            );
    }

    @Bean
    public GroupedOpenApi asistenciaApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Asistencias")
                .pathsToMatch("/api/v*/asistencias/**")
                .build();
    }


    @Bean
    public GroupedOpenApi actividadesApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Actividades")
                .pathsToMatch(
                    "/api/v*/actividades/**" , 
                    "/api/v*/estados-actividad/**",
                    "/api/v*/tipos-actividad/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi certificacionesApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Certificaciones")
                .pathsToMatch("/api/v*/certificaciones/**")
                .build();
    }

    @Bean
    public GroupedOpenApi informeApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Informes")
                .pathsToMatch(
                    "/api/v*/informes/**" ,
                    "/api/v*/tipos-informe"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi inscripcionaesApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Inscripciones")
                .pathsToMatch(
                    "/api/v1/inscripciones",
                    "/api/v*/estados-inscripcion/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi notificacionesApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Notificaciones")
                .pathsToMatch(
                    "/api/v*/notificaciones/**",
                    "/api/v*/estados-notificacion/**",
                    "/api/v*/tipos-notificacion/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi sesionesApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Sesiones")
                .pathsToMatch("/api/v*/sesiones/**")
                .build();
    }

    @Bean
    public GroupedOpenApi usuariosApi() {
        return GroupedOpenApi.builder()
                .group("Modulo Usuarios")
                .pathsToMatch(
                    "/api/v*/usuarios/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi openApis() {
        return GroupedOpenApi.builder()
                .group("APIs libres")
                .pathsToMatch(
                    "/api/v*/usuarios/auth/**",
                    "/" , 
                    "/api/v*/actividades/listar" , 
                    "/api/v*/actividades/obtener/**" , 
                    "/api/v*/{any}/health",
                    "/api/v1/usuarios/participante/registrar"
                )
                .build();
    }
}