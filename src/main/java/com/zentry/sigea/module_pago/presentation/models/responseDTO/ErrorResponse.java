package com.zentry.sigea.module_pago.presentation.models.responseDTO;

public class ErrorResponse {
    private boolean success;
    private String error;
    private String mensaje;
    private Integer statusCode;
    
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }
    
    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    
    public static class ErrorResponseBuilder {
        private ErrorResponse response = new ErrorResponse();
        
        public ErrorResponseBuilder success(boolean success) {
            response.success = success;
            return this;
        }
        
        public ErrorResponseBuilder error(String error) {
            response.error = error;
            return this;
        }
        
        public ErrorResponseBuilder mensaje(String mensaje) {
            response.mensaje = mensaje;
            return this;
        }
        
        public ErrorResponseBuilder statusCode(Integer code) {
            response.statusCode = code;
            return this;
        }
        
        public ErrorResponse build() {
            return response;
        }
    }
}