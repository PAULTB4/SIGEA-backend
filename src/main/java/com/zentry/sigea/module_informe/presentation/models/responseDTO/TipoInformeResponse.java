package com.zentry.sigea.module_informe.presentation.models.responseDTO;

import com.zentry.sigea.module_informe.infrastructure.database.entities.TipoInformeEntity;

public class TipoInformeResponse {
    private final String codigo;
    private final String etiqueta;

    public TipoInformeResponse(String codigo, String etiqueta) {
        this.codigo = codigo;
        this.etiqueta = etiqueta;
    }

    public static TipoInformeResponse fromEntity(TipoInformeEntity entity) {
        return new TipoInformeResponse(
            entity.getCodigo(),
            entity.getEtiqueta()
        );
    }

    public String getCodigo() { return codigo; }
    public String getEtiqueta() { return etiqueta; }
}
