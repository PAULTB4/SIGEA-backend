package com.zentry.sigea.module_certificaciones.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zentry.sigea.module_certificaciones.infrastructure.database.entities.TipoValidadorEntity;

@Repository
public interface TipoValidadorRepository extends JpaRepository<TipoValidadorEntity, UUID> {
    
    /**
     * Busca un tipo de validador por su código
     * @param codigo El código del tipo (QR, HASH, URL_PUBLICA, OCSP)
     * @return Optional con el tipo encontrado
     */
    Optional<TipoValidadorEntity> findByCodigo(String codigo);
    
    /**
     * Verifica si existe un tipo con el código dado
     * @param codigo El código a verificar
     * @return true si existe, false caso contrario
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Busca tipos por etiqueta (búsqueda parcial)
     * @param etiqueta Texto a buscar en la etiqueta
     * @return Lista de tipos que coinciden
     */
    @Query("SELECT tv FROM TipoValidadorEntity tv WHERE LOWER(tv.etiqueta) LIKE LOWER(CONCAT('%', :etiqueta, '%'))")
    java.util.List<TipoValidadorEntity> findByEtiquetaContainingIgnoreCase(@Param("etiqueta") String etiqueta);
}