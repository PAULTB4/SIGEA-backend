package com.zentry.sigea.module_informe.presentation.models.responseDTO;

public class AdjuntoResponse {
    private String id;
    private String nombreArchivo;
    private String url;
    private String tipoMime;
    private long tamano;

    public AdjuntoResponse() {}

    public AdjuntoResponse(String id, String nombreArchivo, String url, String tipoMime, long tamano) {
        this.id = id;
        this.nombreArchivo = nombreArchivo;
        this.url = url;
        this.tipoMime = tipoMime;
        this.tamano = tamano;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipoMime() {
        return tipoMime;
    }
    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public long getTamano() {
        return tamano;
    }
    public void setTamano(long tamano) {
        this.tamano = tamano;
    }
}