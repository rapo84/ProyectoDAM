package com.example.m13actividad2.Actividades;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.ProductoAdapter;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_productos);

        // 1) Recupera el ID del local o usa "sisas" por defecto
        String idLocal = getIntent().getStringExtra("localId");
        if (idLocal == null) idLocal = "sisas";

        // 2) RecyclerView + Adapter
        recyclerView = findViewById(R.id.rVListarProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productos = new ArrayList<>();
        adapter   = new ProductoAdapter(productos);
        recyclerView.setAdapter(adapter);

        // 3) AJUSTA AQUÍ el nombre exacto de tus nodos en Firebase:
        productosRef = FirebaseDatabase.getInstance()
                .getReference("Locales")      // ← tu raíz se llama "Locales"
                .child(idLocal)               // ← "sisas", "EL fogon", "empanadas", …
                .child("Productos");          // ← coincide con "Productos" (mayúscula “P”)

        Log.d(TAG, "Firebase ref: " + productosRef);

        // 4) Métdodo que lee todas las categorías dinámicamente
        cargarProductosDesdeFirebase();
    }

    private void cargarProductosDesdeFirebase() {
        productosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: ref=" + snapshot.getRef() +
                        "  hijos=" + snapshot.getChildrenCount());

                productos.clear();
                for (DataSnapshot categoriaSnap : snapshot.getChildren()) {
                    Log.d(TAG, "  Categoría: " + categoriaSnap.getKey() +
                            " (items=" + categoriaSnap.getChildrenCount() + ")");
                    for (DataSnapshot prodSnap : categoriaSnap.getChildren()) {
                        Producto p = prodSnap.getValue(Producto.class);
                        if (p != null) {
                            Log.d(TAG, "    → " + p.getNombre());
                            productos.add(p);
                        }
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
