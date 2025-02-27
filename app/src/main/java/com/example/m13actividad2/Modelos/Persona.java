package com.example.m13actividad2.Modelos;

import java.util.Objects;


public class Persona {
    
    private String uid;
    private String dni;
    private String nombre;
    private String apellidos;
    private int telefono;
    private String correo;
    private String password;
    private String categoria;

    public Persona(String UID) {
        this.uid = UID;
    }

    public Persona(String uid,String dni, String nombre, String apellidos, String categoria, String correo) {
        this.uid= uid;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.categoria = categoria;
        this.correo = correo;
    }

    public Persona(String UID, String dni, String nombre, String apellidos, int telefono, String correo, String password, String categoria) {
        this.uid = UID;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.password = password;
        this.categoria = categoria;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCategoria() { return categoria;
    }

    public void setCategoria(String categoria) { this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Persona other = (Persona) obj;
        if (this.uid != other.uid) {
            return false;
        }
        return Objects.equals(this.dni, other.dni);
    }

    
    
}
