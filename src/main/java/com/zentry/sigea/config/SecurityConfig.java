package com.zentry.sigea.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.zentry.sigea.module_usuarios.services.TokenUsuarioService;
import com.zentry.sigea.security.CustomAccessDeniedHandler;
import com.zentry.sigea.security.CustomAuthenticationEntryPoint;
import com.zentry.sigea.security.JwtAuthenticationFilter;
import com.zentry.sigea.security.JwtBlacklistService;
import com.zentry.sigea.security.JwtUtil;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Value("${sigea.public.frontend.domain}")
    private String sigeaPublicFrontendDomain;

    @Value("${sigea.public.backend.domain}")
    private String sigeaPublicBackendDomain;

    @Value("${sigea.allowed.origin.backend.localhost.path}")
    private String sigeaAllowedOriginBakendLocalhostPath;
    @Value("${sigea.allowed.origin.frontend.localhost.path}")
    private String sigeaAllowedOriginFrontendLocalhostPath;

    public SecurityConfig(
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
        CustomAccessDeniedHandler customAccessDeniedHandler
    ){
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenUsuarioService tokenUsuarioService, JwtUtil jwtUtil , JwtBlacklistService jwtBlacklistService , CustomAuthenticationEntryPoint customAuthenticationEntryPoint){
        return new JwtAuthenticationFilter(jwtUtil, tokenUsuarioService , jwtBlacklistService , customAuthenticationEntryPoint);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http , JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/api/v*/usuarios/auth/**", 
                            "/" , 
                            "/v*/api-docs.yaml",

                            // Permitir el acceso libre para ver las actividades en la pagina principal
                            "/api/v*/actividades/listar",
                            "/api/v*/actividades/obtener/**",
                            "/api/v*/actividad/banner/imagen/**",

                            "/api/v*/tipos-actividad/listar",
                            "/api/v*/estados-actividad/listar",

                            "/api/v*/usuarios/participante/registrar",
                            "/api/v*/usuarios/validar-correo/**", // <-- Permitir validación de correo sin autenticación

                            "/api/v*/sesiones/listar",
                            "/api/v*/sesiones/obtener/**",

                            "/api/v*/{any}/health"
                        ).permitAll()
                        .requestMatchers(
                            "/swagger-ui/**", 
                            "/swagger-ui.html", 
                            "/v*/api-docs/**", 
                            "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
            sigeaAllowedOriginBakendLocalhostPath,
            sigeaAllowedOriginFrontendLocalhostPath,
            sigeaPublicBackendDomain,
            sigeaPublicFrontendDomain
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept"
        ));
        config.setExposedHeaders(List.of("Authorization" , "X-New-Token"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
