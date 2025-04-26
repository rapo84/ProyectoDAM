package com.example.m13actividad2.interfaces;

import com.example.m13actividad2.Modelos.Producto;

import java.util.List;

public interface ProductosCargadosListener {
    void callbackProductosCargados(List<Producto> productos);
}
