package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.R;

public class GestionEmpleados extends AppCompatActivity {
    private Button agregarEmpleado, listarEmpleados, eliminarEmpleados, cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_empleados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        agregarEmpleado= findViewById(R.id.bt_GE_agregarEmpleado);
        listarEmpleados= findViewById(R.id.bt_GE_ListarEmpleado);
        eliminarEmpleados = findViewById(R.id.bt_GE_EliminarEmpleado);
        cerrarSesion = findViewById(R.id.bt_GE_logout);

        agregarEmpleado.setOnClickListener(view -> {
            Intent intent = new Intent(GestionEmpleados.this, AdminActivityRegistrar.class);
            startActivity(intent);

        });

        listarEmpleados.setOnClickListener(view -> {
            Intent intent = new Intent( GestionEmpleados.this, AdminActivityListar.class);
            startActivity(intent);

        });

        eliminarEmpleados.setOnClickListener(view -> {
            Intent intent = new Intent(GestionEmpleados.this, AdminActivityEliminar.class);
            startActivity(intent);

        });

        cerrarSesion.setOnClickListener(view -> {
            Intent intent = new Intent(GestionEmpleados.this, Ventana_Inicial.class);
            startActivity(intent);
            finish();
        });

    }

}