package com.zentry.sigea.module_informe.core.entities;

public class TipoInformeDomainEntity {
    private String id; 
    private String codigo;
    private String etiqueta;

    public TipoInformeDomainEntity() {}

    public TipoInformeDomainEntity(String id, String codigo, String etiqueta) {
        this.id = id;
        this.codigo = codigo;
        this.etiqueta = etiqueta;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
}
