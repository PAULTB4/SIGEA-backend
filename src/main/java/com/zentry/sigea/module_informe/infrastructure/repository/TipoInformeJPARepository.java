package com.zentry.sigea.module_informe.infrastructure.repository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;
public interface TipoInformeJPARepository extends JpaRepository<TipoInformeEntity, UUID> {
    Optional<TipoInformeEntity> findByCodigo(String codigo);
    List<TipoInformeEntity> findByEtiqueta(String etiqueta);
    Optional<TipoInformeEntity> findByCodigoIgnoreCase(String codigo);
}
