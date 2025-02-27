package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m13actividad2.Adaptadores.EmpleadoAdapter;
import com.example.m13actividad2.Adaptadores.Utilidad;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.R;


import java.util.ArrayList;
import java.util.List;

public class AdminActivityListar extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmpleadoAdapter adapter;
    private List<Persona> listaPersonas;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listar);
        EdgeToEdge.enable(this); //elimina la barra de informacion del movil (donde sale la bateria señal y eso
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {         //evita que los objetos del sistema cubran la interfaz ej: que el teclado cubra algun boton
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());             //que este en la parte de abajod e la pantalla, con esto se hace accesible el botton
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logout = findViewById(R.id.bt_admin_volver);
        recyclerView = findViewById(R.id.recyclerViewEmpleados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listaPersonas = new ArrayList<>();

        adapter = new EmpleadoAdapter(listaPersonas, this);
        recyclerView.setAdapter(adapter);

        Utilidad.leerTrabajadores(this, listaPersonas,adapter);

        logout.setOnClickListener(view -> {
            Intent intent= new Intent(this, OpcionesDeAdmin.class);
            startActivity(intent);
            finish();
        });

    }



}