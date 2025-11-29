package com.zentry.sigea.module_certificaciones.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.EstadoCertificadoEntity;

@Repository
public interface EstadoCertificadoRepository extends JpaRepository<EstadoCertificadoEntity, UUID> {
    
    /**
     * Busca un estado de certificado por su código
     * @param codigo El código del estado (EMITIDO, REVOCADO, SUSPENDIDO)
     * @return Optional con el estado encontrado
     */
    Optional<EstadoCertificadoEntity> findByCodigo(String codigo);
    
    /**
     * Verifica si existe un estado con el código dado
     * @param codigo El código a verificar
     * @return true si existe, false caso contrario
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Busca estados por etiqueta (búsqueda parcial)
     * @param etiqueta Texto a buscar en la etiqueta
     * @return Lista de estados que coinciden
     */
    @Query("SELECT ec FROM EstadoCertificadoEntity ec WHERE LOWER(ec.etiqueta) LIKE LOWER(CONCAT('%', :etiqueta, '%'))")
    java.util.List<EstadoCertificadoEntity> findByEtiquetaContainingIgnoreCase(@Param("etiqueta") String etiqueta);
}