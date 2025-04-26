package com.example.m13actividad2.Actividades;

import android.os.Bundle;

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
private DatabaseReference dbRef;


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

       dbRef = FirebaseDatabase.getInstance().getReference("productos");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                producto.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Producto p = data.getValue(Producto.class);
                    producto.add(p);
                }
                adapter.notifyDataSetChanged(); // actualizar vista
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // manejar errores
            }
        });
    }
}
