package com.example.m13actividad2.firebaseServerSdk;

public interface RoleCheckCallback {
    void onRoleChecked(boolean aceptado);
    void onRoleReceived(String CateRol, String localex);
}
/*
INTERFAZ USADA PARA CUANDO USEMOS EL CHECKROLE NOS PUEDA REGRESAR UN BOOLEANO DEPENDIENDO DE LA RESPUESTA O LOS ROLES QUE TENGA ESE USUARIO (DEPENDE DE CUAL METODO DESARROLLEMOS
 */
