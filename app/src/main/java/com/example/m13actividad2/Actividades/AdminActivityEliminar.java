package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m13actividad2.Adaptadores.EmpleadoAdapterBorrar;
import com.example.m13actividad2.utils.Utilidad;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.R;


import java.util.ArrayList;
import java.util.List;

public class AdminActivityEliminar extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EmpleadoAdapterBorrar adapter;
    private List<Persona> listaPersonas;
    private Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_eliminar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        volver = findViewById(R.id.bt_admin_eliminar_volver);
        recyclerView = findViewById(R.id.recyclerViewEmpleados_eliminar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listaPersonas = new ArrayList<>();

        adapter = new EmpleadoAdapterBorrar(listaPersonas, this);
        recyclerView.setAdapter(adapter);


        Utilidad.leerTrabajadores(this,listaPersonas, adapter);

        volver.setOnClickListener(view -> {
            Intent intent= new Intent(this, OpcionesDeAdmin.class);
            startActivity(intent);
            finish();
        });

    }


}