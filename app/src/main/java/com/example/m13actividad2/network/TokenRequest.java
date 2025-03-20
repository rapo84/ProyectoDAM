package com.example.m13actividad2.network;
/*
CLASE PARA PODER MANEJAR EL TOKEN
 */
public class TokenRequest {
    private String idToken;

    public TokenRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
