package com.zentry.sigea.module_usuarios.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zentry.sigea.module_usuarios.core.entities.TokenUsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRepository;
import com.zentry.sigea.module_usuarios.core.repositories.IUsuarioRolRepository;
import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.LoginResponseDTO;
import com.zentry.sigea.module_usuarios.services.usecases.EliminarUsuarioUseCase;
import com.zentry.sigea.security.JwtBlacklistService;
import com.zentry.sigea.security.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class UsuarioService {
    /**
     * Retorna los IDs de los usuarios que tienen el rol 'Participante'
     */
    public List<String> getUsuariosIdsPorRolParticipante() {
        return usuarioRepository.findAll().stream()
            .filter(usuario -> usuarioRolRepository.findRolesByUsuarioId(usuario.getId())
                .stream()
                .anyMatch(rol -> rol.getNombreRol().equalsIgnoreCase("Participante")))
            .map(UsuarioDomainEntity::getId)
            .collect(Collectors.toList());
    }

    private final JwtBlacklistService jwtBlacklistService;
    private final IUsuarioRepository usuarioRepository;
    private final IUsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUsuarioService tokenUsuarioService;
    private final JwtUtil jwtUtil;

    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;

    public UsuarioService(
        IUsuarioRepository usuarioRepository,
        IUsuarioRolRepository usuarioRolRepository,
        PasswordEncoder passwordEncoder,
        TokenUsuarioService tokenUsuarioService , 
        JwtUtil jwtUtil , 
        JwtBlacklistService jwtBlacklistService,
        EliminarUsuarioUseCase eliminarUsuarioUseCase

    ){
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenUsuarioService = tokenUsuarioService;
        this.jwtUtil = jwtUtil;
        this.jwtBlacklistService = jwtBlacklistService;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
    }

    public LoginResponseDTO login(String correo , String password , Boolean rememberMe){
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

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        loginResponseDTO.setAccessToken(accessToken);
        loginResponseDTO.setCorreoVerificado(usuario.getCorreoVerificado());
        loginResponseDTO.setRefreshToken(tokenUsuarioDomainEntity.getToken());
        loginResponseDTO.setIdRefreshToken(tokenUsuarioDomainEntity.getId());

        return loginResponseDTO;
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

    public String eliminarUsuario(String usuarioId){
        return eliminarUsuarioUseCase.execute(usuarioId);
    }
}