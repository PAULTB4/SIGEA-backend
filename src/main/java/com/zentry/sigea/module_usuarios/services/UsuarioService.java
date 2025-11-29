package com.zentry.sigea.module_usuarios.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.security.JwtBlacklistService;
import com.zentry.sigea.security.JwtUtil;

import io.jsonwebtoken.Claims;

import java.util.Map;

@Service
public class UsuarioService {

    private final JwtBlacklistService jwtBlacklistService;
    private final IUsuarioRepository usuarioRepository;
    private final IUsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUsuarioService tokenUsuarioService;
    private final JwtUtil jwtUtil;

    public UsuarioService(
        IUsuarioRepository usuarioRepository,
        IUsuarioRolRepository usuarioRolRepository,
        PasswordEncoder passwordEncoder,
        TokenUsuarioService tokenUsuarioService , 
        JwtUtil jwtUtil
    , JwtBlacklistService jwtBlacklistService){
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUsuarioService = tokenUsuarioService;
        this.jwtUtil = jwtUtil;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    public Map<String , String> login(String correo , String password , Boolean rememberMe){
        UsuarioDomainEntity usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!passwordEncoder.matches(password , usuario.getPasswordHash())){
            throw new RuntimeException("Contrasñea incorrecta.");
        }

        List<String> roles = usuarioRolRepository.findRolesByUsuarioId(usuario.getId())
            .stream()
            .map(r -> r.getNombreRol())
            .collect(Collectors.toList());

        String accessToken = jwtUtil.generateToken(
            usuario.getCorreo() , 
            usuario.getId() ,
            roles
        );

        TokenUsuarioDomainEntity tokenUsuarioDomainEntity = tokenUsuarioService.createRefreshToken(usuario.getId(), rememberMe);

        return Map.of(
            "Access Token" , accessToken , 
            "Refresh Token" , tokenUsuarioDomainEntity.getToken() , 
            "ID Refresh Token" , tokenUsuarioDomainEntity.getId()
        );
    }

    public String logout(String usuarioId , String accessToken){
        try {
            tokenUsuarioService.deleteByUsuario_ID(usuarioId);

            Claims claims = jwtUtil.extractClaims(accessToken);
            jwtBlacklistService.add(accessToken, claims.getExpiration());

            return "Logout exitoso";
        } catch (Exception e) {
            return "El cierre de sesion fallo";
        }
    }
}