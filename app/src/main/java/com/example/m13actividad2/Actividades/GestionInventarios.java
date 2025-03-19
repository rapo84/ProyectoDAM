package com.example.m13actividad2.Actividades;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.Adaptadores.Utilidad;
import com.example.m13actividad2.R;

public class GestionInventarios extends AppCompatActivity {

    private Button Salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_inventarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Salir= findViewById(R.id.bt_inventario_logout);

        Salir.setOnClickListener(view -> {
            Utilidad.cerrarSesionYRedirigir(this, Ventana_Inicial.class);
        });
    }
}