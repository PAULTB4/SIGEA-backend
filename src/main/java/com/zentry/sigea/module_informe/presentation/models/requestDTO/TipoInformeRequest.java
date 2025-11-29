package com.zentry.sigea.module_informe.presentation.models.requestDTO;

public class TipoInformeRequest {

    private final String codigo;
    private final String etiqueta;

    public TipoInformeRequest(String codigo, String etiqueta) {
        this.codigo = codigo;
        this.etiqueta = etiqueta;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
