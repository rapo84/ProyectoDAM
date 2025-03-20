package com.example.m13actividad2.network;

/*
CLASE CREADA PARA PODER HACER LA LLAMADA (CON LA INTERFACE apiservice) CON ESTA
CLASE ESTRUCTURAMOS LA OBTENCION DE LOS CLAIMS DE LOS USURAIOS REGISTRADOS A LOS QUE YA SE
LES HA ASIGNADO UNOS CLAIMS PERSONALIZADOS
 */

public class ClaimsResponse {
    private boolean success;
    private String role;
    private String local;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    // Añadimos un método toString para mostrar los claims de forma legible
    @Override
    public String toString() {
        return "role: " + role + ", local: " + local;
    }
}
