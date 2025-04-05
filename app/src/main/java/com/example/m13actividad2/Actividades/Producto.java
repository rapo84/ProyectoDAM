package com.example.m13actividad2.Actividades;

public class Producto {

    private String nombre;
    private double precio;
    private String descripcion;

    public Producto(){

    }

    public Producto(String nombre, double precio, String descripcion){
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public String getNombre(){
        return  nombre;
    }

    public double getPrecio(){
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
