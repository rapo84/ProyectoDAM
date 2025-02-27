package com.example.m13actividad2.firebaseServerSdk;

import java.util.ArrayList;

/*
esta interfaz es para poder obtener la lista de los locales ya que si no lo hacemos así,
 al ser las consultas a firebase asincronas, el programa regresa la lista (ver codigo donde se llama)
sin esperar a que firebase regrese los datos por eso hay que usar el callback
 */
public interface LocalesCallback {
    void onCallback(ArrayList<String> locales);
}
