package com.example.m13actividad2.Actividades;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.ProductoAdapter;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.Utilidad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListarProductos extends AppCompatActivity {
    private static final String TAG = "ListarProductos";

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productos;
    private DatabaseReference productosRef;
    private String categoria;
    private Spinner spinnercat;
    private ImageButton buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //  Recupera el ID del local o usa "sisas" por defecto
        String idLocal = Utilidad.recupernombrelocal(this);

        //  RecyclerView + Adapter otros
        recyclerView = findViewById(R.id.rVListarProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productos = new ArrayList<>();
        adapter   = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);
        spinnercat = findViewById(R.id.spinner_seleccion_Cat_list);
        buscar = findViewById(R.id.bt_ListarProd_buscar);

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

        // AJUSTA AQUÍ el nombre exacto de tus nodos en Firebase:
        productosRef = FirebaseDatabase.getInstance()
                .getReference("Locales")
                .child(idLocal)
                .child("Productos");

        Log.d(TAG, "Firebase ref: " + productosRef);

        buscar.setOnClickListener(view -> {
            categoria = spinnercat.getSelectedItem().toString();
            cargarProductosDesdeFirebase(categoria);
            //Utilidad.leerProductos(this,productos,adapter,categoria);
        });
    }



    private void cargarProductosDesdeFirebase(String categoria) {
        productosRef.child(categoria).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productos.clear();
                    for (DataSnapshot prodSnap : snapshot.getChildren()) {
                        Producto p = prodSnap.getValue(Producto.class);
                        if (p != null) {
                            Log.d(TAG, "    → " + p.getNombre());
                            productos.add(p);
                        }
                    }
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Total productos cargados = " + productos.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error leyendo productos", error.toException());
            }

        });
    }
}
