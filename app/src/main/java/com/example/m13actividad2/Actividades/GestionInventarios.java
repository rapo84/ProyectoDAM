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

public class GestionInventarios extends AppCompatActivity {

    private Button AgregarProducto, ListarProductos, ModificarProductos, cerrarSesion;

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

        AgregarProducto = findViewById(R.id.bt_inventario_agregar);
        ListarProductos = findViewById(R.id.bt_inventario_listar);
        ModificarProductos = findViewById(R.id.bt_inventario_modificar);
        cerrarSesion = findViewById(R.id.bt_GE_logout2);

        ListarProductos.setOnClickListener(view -> {
            Intent intent = new Intent(GestionInventarios.this, ListarProductos.class);
            startActivity(intent);
        });

        ModificarProductos.setOnClickListener(view -> {
            Intent intent = new Intent(GestionInventarios.this,ModRemoveProductos.class);
            startActivity(intent);
        });

        AgregarProducto.setOnClickListener(view -> {
            Intent intent = new Intent(GestionInventarios.this,AgregarProductos.class);
            startActivity(intent);
        });

        cerrarSesion.setOnClickListener(view -> {
            Intent intent = new Intent(GestionInventarios.this, Ventana_Inicial.class);
            startActivity(intent);
            finish();
        });




    }
}