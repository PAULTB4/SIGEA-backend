package com.zentry.sigea.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String, Object> responseBody = Map.of(
            "success", false,
            "message", "No tienes los permisos suficientes para acceder a este recurso."
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), responseBody);
    }
}
