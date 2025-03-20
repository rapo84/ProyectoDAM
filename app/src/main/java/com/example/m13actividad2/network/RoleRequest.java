package com.example.m13actividad2.network;
/*
CLASE PARA PODER MANEJAR LA PETICION DE ROLREQUEST YA QUE PASAMOS LA INFO COMO UN OBJETO  (REVISAR LO DE PASAR LA INFO COMO OBJETO)
 */
public class RoleRequest {
    private String uid;
    private String local;
    private String role;

    public RoleRequest(String uid, String local, String role) {
        this.uid = uid;
        this.local = local;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}