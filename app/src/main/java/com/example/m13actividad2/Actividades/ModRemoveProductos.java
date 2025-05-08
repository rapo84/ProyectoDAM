package com.example.m13actividad2.Actividades;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.ModProductAdapter;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.Utilidad;

import java.util.ArrayList;
import java.util.List;

public class ModRemoveProductos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ModProductAdapter adapter;
    private List<Producto> productos;
    private String categoria;
    private Spinner spinnercat;
    private ImageButton buscar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_remove_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewModProductos);
        spinnercat = findViewById(R.id.spinner_select_Cat);
        buscar = findViewById(R.id.bt_modproduct_buscar);

        String[] categorias = {"Selecciona una categoria","Bebidas frias", "Bebidas calientes", "Bolleria", "Pasteleria", "Comidas", "Desayunos", "Meriendas"};
        ArrayAdapter<String> spadapter = new ArrayAdapter<String>(this, R.layout.item_spinner_personalizado, categorias){
            @Override
            public boolean isEnabled(int position) {
                // Deshabilita la primera opción
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.texto_spinner);
                if (position == 0) {
                    // Color gris para la opción deshabilitada
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };

        spinnercat.setAdapter(spadapter);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productos = new ArrayList<>();

        adapter = new ModProductAdapter(ModRemoveProductos.this, productos); // iniciamo el adaptador
        recyclerView.setAdapter(adapter); // acoplamos el adaptador con el reciclerview



        buscar.setOnClickListener(view -> {
            categoria = spinnercat.getSelectedItem().toString();
            Utilidad.leerProductos(this,productos,adapter,categoria);
        });



    }
}