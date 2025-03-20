package com.example.m13actividad2.network;

/*
CLASE PARA PODER MANEJAR LA PETICION DE ROLREQUEST PERO SOLO PARA EL SUPER USER (ESTO SE UNA SOLO UNA VEZ)
 */
public class RoleRequestAdmin {
    private String uid;
    private String local;
    private String role;

    public RoleRequestAdmin(String uid, String role) {
        this.uid = uid;
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

}