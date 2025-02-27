package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.Adaptadores.Utilidad;
import com.example.m13actividad2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpcionesDeAdmin extends AppCompatActivity {
    private Button Registrar, Eliminar, Listar, Salir, ModoEmpleado;

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

        Registrar=findViewById(R.id.bt_admin_register);
        Eliminar=findViewById(R.id.bt_admin_delete);
        Listar=findViewById(R.id.bt_admin_list);
        Salir= findViewById(R.id.bt_admin_salir);
        ModoEmpleado= findViewById(R.id.bt_admin_cambiar_modo_empleado);

        Registrar.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, AdminActivityRegistrar.class);
            startActivity(intent);
            finish();

        });


        Eliminar.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, AdminActivityEliminar.class);
            startActivity(intent);
            finish();

        });


        Listar.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, AdminActivityListar.class);
            startActivity(intent);
            finish();
        });

        Salir.setOnClickListener(view -> {
            Utilidad.cerrarSesionYRedirigir(this, Ventana_Inicial.class);
        });

        ModoEmpleado.setOnClickListener(view -> {
            Intent intent = new Intent(OpcionesDeAdmin.this, EmpleadoActivity.class);
            startActivity(intent);
            finish();
        });

    }

}