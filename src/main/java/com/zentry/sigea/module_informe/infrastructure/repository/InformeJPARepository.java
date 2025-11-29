package com.zentry.sigea.module_informe.infrastructure.repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zentry.sigea.module_informe.infrastructure.database.entities.InformeEntity;

public interface InformeJPARepository extends JpaRepository<InformeEntity, UUID> {
    List<InformeEntity> findByActividad_Id(UUID actividadId);
    List<InformeEntity> findByTipoInforme_Id(UUID tipoInformeId);
    List<InformeEntity> findByFechaSubida(LocalDate fechaSubida);
    List<InformeEntity> findByActividad_IdAndTipoInforme_Id(UUID actividadId, UUID tipoInformeId);
    List<InformeEntity> findByFechaSubidaBetween(LocalDate start, LocalDate end);
}
