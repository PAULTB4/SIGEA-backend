package com.zentry.sigea.module_usuarios.services.serviceDTO;

import java.util.ArrayList;
import java.util.List;

import com.zentry.sigea.module_usuarios.core.entities.UsuarioDomainEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnviarEstadisticasUsuariosServiceDTO {
    private Integer totalRegisteredUsers;
    private List<UsuarioDomainEntity> listUsuarioDomainEntities = new ArrayList<>();

    private Integer totalUsuariosOrganizador;
    private List<UsuarioDomainEntity> listUsuariosOrganizador = new ArrayList<>();

    private Integer totalUsuariosParticipante;
    private List<UsuarioDomainEntity> listUsuariosParticipante = new ArrayList<>();
}
