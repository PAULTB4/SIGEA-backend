package com.zentry.sigea.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.services.TokenUsuarioService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final TokenUsuarioService tokenUsuarioService;
    private final JwtBlacklistService jwtBlacklistService;
    private final CustomAuthenticationEntryPoint authEntryPoint;

    public JwtAuthenticationFilter(
        JwtUtil jwtUtil , 
        TokenUsuarioService tokenUsuarioService, 
        JwtBlacklistService jwtBlacklistService,
        CustomAuthenticationEntryPoint authEntryPoint
    ){
        this.jwtUtil = jwtUtil;
        this.tokenUsuarioService = tokenUsuarioService;
        this.jwtBlacklistService = jwtBlacklistService;
        this.authEntryPoint = authEntryPoint;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response , 
        FilterChain filterChain
    ) throws ServletException , IOException{

        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);

            if(jwtBlacklistService.isBlacklisted(token)) {
                SecurityContextHolder.clearContext();
                authEntryPoint.commence(
                        request, response,
                        new org.springframework.security.core.AuthenticationException("Token inválido o cerrado") {}
                );
                return;
            }

            if(!jwtUtil.isExpired(token)){ // Token expirado no continuar
                Claims claims = jwtUtil.extractClaims(token);
                setAuthentication(claims);

                if (jwtUtil.willExpireSoon(token)) {
                    String newToken = jwtUtil.generateToken(
                        claims.getSubject(),
                        claims.get("usuarioId", String.class),
                        claims.get("roles", List.class)
                    );
                    response.setHeader("X-New-Token", newToken);
                }

                filterChain.doFilter(request, response);
                return;
            }
            
            String unhashedRefreshToken = null;
            String idRefreshToken = null;
            if (request.getCookies() != null) {
                for(Cookie cookie : request.getCookies()){
                    if(cookie.getName().equals("refreshToken")){
                        unhashedRefreshToken = cookie.getValue();
                    } else if (cookie.getName().equals("idRefreshToken")) {
                        idRefreshToken = cookie.getValue();
                    }
                }
            }

            if(unhashedRefreshToken == null && idRefreshToken == null){
                SecurityContextHolder.clearContext();
                authEntryPoint.commence(
                        request, response,
                        new org.springframework.security.core.AuthenticationException("Debes iniciar sesión") {}
                );
                return;
            }

            TokenUsuarioDomainEntity tokenUsuarioDomainEntity = tokenUsuarioService.findByUnhashedTokenAndId(
                unhashedRefreshToken, 
                idRefreshToken
            );

            tokenUsuarioService.verifyExpiration(tokenUsuarioDomainEntity);

            Claims claims = jwtUtil.extractClaimsFromExpired(token);
            String newAccessToken = jwtUtil.generateToken(
                claims.getSubject(), 
                claims.get("usuarioId", String.class),
                claims.get("roles", List.class)
            );

            response.setHeader("X-New-Token", newAccessToken);
            setAuthentication(claims);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            authEntryPoint.commence(
                    request, response,
                    new org.springframework.security.core.AuthenticationException("No autenticado") {}
            );
            return;
        }
    }

    private void setAuthentication(Claims claims) {
        String usuarioId = claims.get("usuarioId", String.class);
        String email = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        var authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());

        UsuarioAuthInfo usuarioAuthInfo = new UsuarioAuthInfo(usuarioId, email);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                usuarioAuthInfo, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
