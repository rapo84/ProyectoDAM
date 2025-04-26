package com.example.m13actividad2.Modelos;

import java.util.HashMap;
import java.util.Map;

public class Mesas {

    private int numero_mesa;
    private String estado;  // (opcional) Estado de la mesa (por ejemplo, "ocupada", "libre")
    private double total;   // (opcional) Total de la mesa, si lo deseas calcular

    public Mesas(int numero_mesa) {
        this.numero_mesa = numero_mesa;
        this.estado = "libre"; // Inicialmente está libre
        this.total = 0.0;
    }

    // Getters y setters
    public int getNumero_mesa() {
        return numero_mesa;
    }

    public void setNumero_mesa(int numero_mesa) {
        this.numero_mesa = numero_mesa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}

