package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.utils.Utilidad;
import com.example.m13actividad2.R;

public class OpcionesDeAdmin extends AppCompatActivity {
    private Button Salir, ModoEmpleado, GestionInventarios, GestionEstablecimiento, GestionEmpleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opciones_de_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Salir= findViewById(R.id.bt_admin_salir);
        ModoEmpleado= findViewById(R.id.bt_admin_cambiar_modo_empleado);
        GestionInventarios= findViewById(R.id.bt_admin_GestionInventarios);
        GestionEstablecimiento=findViewById(R.id.bt_admin_gestionEstablecimiento);
        GestionEmpleados=findViewById(R.id.bt_admin_gestionEmpleados);


        GestionEstablecimiento.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, GestionEstablecimiento.class);
            startActivity(intent);


        });


        GestionEmpleados.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, GestionEmpleados.class);
            startActivity(intent);

        });

        GestionInventarios.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, GestionInventarios.class);
            startActivity(intent);
        });

        ModoEmpleado.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, EmpleadoActivity.class);
            startActivity(intent);
            finish();
        });

        Salir.setOnClickListener(view -> {
            Utilidad.cerrarSesionYRedirigir(this, Ventana_Inicial.class);
        });

    }

}