package com.zentry.sigea.module_usuarios.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zentry.sigea.module_usuarios.infrastructure.database.entities.RolEntity;

public interface RolJPARepository extends JpaRepository<RolEntity , UUID>{
    public List<RolEntity> findAll();
    public Optional<RolEntity> findById(UUID id);
    public Optional<RolEntity> findByNombreRol(String nombreRol);
    
    @Query(
        """
            SELECT r.id FROM RolEntity r WHERE r.nombreRol = :nombreRol
        """
    )
    public Optional<UUID> findIdByNombreRol(@Param("nombreRol") String nombreRol);
}
