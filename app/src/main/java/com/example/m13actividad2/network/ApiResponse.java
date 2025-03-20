package com.example.m13actividad2.network;
/*
CLASE PARA MANEJAR LA RESPUESTA DEL SERVIDOR NODE
 */
public class ApiResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
