package com.zentry.sigea.module_usuarios.presentation.models.mappers;

import com.zentry.sigea.module_usuarios.presentation.models.responseDTO.ListarUsuariosResponseDTO;
import com.zentry.sigea.module_usuarios.services.serviceDTO.ListarUsuariosServiceDTO;

public class ListarUsuariosMapper {
    public static ListarUsuariosResponseDTO serviceToResponse(
        ListarUsuariosServiceDTO listarUsuariosServiceDTO
    ){
        ListarUsuariosResponseDTO obtenerUsuarioResponseDTO = new ListarUsuariosResponseDTO();

        obtenerUsuarioResponseDTO.setId(listarUsuariosServiceDTO.getId());
        obtenerUsuarioResponseDTO.setNombres(listarUsuariosServiceDTO.getNombres());
        obtenerUsuarioResponseDTO.setApellidos(listarUsuariosServiceDTO.getApellidos());
        obtenerUsuarioResponseDTO.setCorreo(listarUsuariosServiceDTO.getCorreo());
        obtenerUsuarioResponseDTO.setDni(listarUsuariosServiceDTO.getDni());
        obtenerUsuarioResponseDTO.setCorreoVerificado(listarUsuariosServiceDTO.getCorreoVerificado());
        obtenerUsuarioResponseDTO.setCreatedAt(listarUsuariosServiceDTO.getCreatedAt());
        obtenerUsuarioResponseDTO.setUpdatedAt(listarUsuariosServiceDTO.getUpdatedAt());
        obtenerUsuarioResponseDTO.setTelefono(listarUsuariosServiceDTO.getTelefono());
        obtenerUsuarioResponseDTO.setExtensionTelefonica(listarUsuariosServiceDTO.getExtensionTelefonica());

        obtenerUsuarioResponseDTO.setNombresRoles(listarUsuariosServiceDTO.getNombresRoles());

        return obtenerUsuarioResponseDTO;
    }
}
