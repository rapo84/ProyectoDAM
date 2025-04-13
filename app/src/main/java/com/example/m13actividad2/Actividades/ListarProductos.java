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
private RecyclerView recyclerView;
private ProductoAdapter adapter;
private List<Producto> producto;
private DatabaseReference productosRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_productos);

       recyclerView = findViewById((R.id.rVListarProductos));
       recyclerView.setLayoutManager(new LinearLayoutManager(this));

       producto = new ArrayList<>();
       adapter = new ProductoAdapter(producto);
       recyclerView.setAdapter(adapter);

        productosRef = FirebaseDatabase.getInstance()
                .getReference("localesTotales")
                .child("sisas") // reemplazar por el nombre del local
                .child("PRODUCTOS");

        cargarProductosDesdeFirebase();
    }

    private void cargarProductosDesdeFirebase() {
        // Leer bebidas frías
        productosRef.child("bebidas_frias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Producto p = data.getValue(Producto.class);
                    producto.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer bebidas frías", error.toException());
            }
        });

        // Leer bebidas calientes
        productosRef.child("bebidas_calientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Producto p = data.getValue(Producto.class);
                    producto.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer bebidas calientes", error.toException());
            }
        });
    }
}
