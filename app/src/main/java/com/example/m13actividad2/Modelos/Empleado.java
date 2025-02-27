package com.example.m13actividad2.Modelos;

public class Empleado extends Persona{
    public Empleado(String UID) {
        super(UID);
    }

    public Empleado(String UID, String dni, String nombre, String apellidos, int telefono, String correo, String password, String categoria) {
        super(UID, dni, nombre, apellidos, telefono, correo, password, categoria);
    }
}
