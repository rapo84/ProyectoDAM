package com.example.m13actividad2.utils;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.Modelos.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UtilidadMesas {

    public static void obtenercategorias(Context context, List<String> lista, RecyclerView.Adapter adapter) {
        String nombrelocal= Utilidad.recupernombrelocal(context);           //Obtenemos el nombre del local a donde pertenece el dispositivo

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales").child(nombrelocal).child("Productos");  // obtenemos la referencia a la bbdd

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lista.clear();                                                                      // Limpiamos la lista antes de agregar nuevos datos
                for (DataSnapshot categoriaSnapshot : snapshot.getChildren()) {
                    String nombreCategoria = categoriaSnapshot.getKey(); // obtiene "las categorias existentes"
                    lista.add(nombreCategoria);
                }
                adapter.notifyDataSetChanged();
                // Ahora puedes usar la lista `categorias` para cargar tu RecyclerView, etc.                                                    // Notificar al RecyclerView que los datos han cambiado y que debe volver a cargar
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());     // NO borrar, ofrece informacion
            }
        });
    }

    public static void cargarProductosPorCategoria(Context context, List<Producto> productos, RecyclerView.Adapter adapter, String categoriaSeleccionada) {
        String nombrelocal= Utilidad.recupernombrelocal(context);           //Obtenemos el nombre del local a donde pertenece el dispositivo

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales").child(nombrelocal).child("Productos").child(categoriaSeleccionada);  // obtenemos la referencia a la bbdd

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productos.clear();                                                                      // Limpiamos la lista antes de agregar nuevos datos
                for (DataSnapshot categoriaSnapshot : snapshot.getChildren()) {
                        String nombre = categoriaSnapshot.child("nombre").getValue(String.class);
                        String categoria = categoriaSnapshot.child("categoria").getValue(String.class);
                        String codigo = categoriaSnapshot.child("codigo").getValue(String.class);
                        Integer cantidad = categoriaSnapshot.child("cantidad").getValue(Integer.class);
                        if (cantidad == null) {
                            cantidad = 0; // en caso de ser null queda a 0 por defecto(no deberia porque cuando se pide el dato en la app se verifica pero no esta demas)
                        }
                        // Obtener el precio como Double
                        Double precio = categoriaSnapshot.child("precio").getValue(Double.class);
                        if (precio == null) {
                            precio = 0.0; // // en caso de ser null queda a 0 por defecto(no deberia porque cuando se pide el dato en la app se verifica pero no esta demas)
                        }


                        productos.add(new Producto(codigo, nombre, precio, categoria, cantidad));
                        Log.d("Producto", "✅ nombre: " + nombre + ", categoria: " + categoria + ", codigo: " + codigo);

                }
                adapter.notifyDataSetChanged();                                                     // Notificar al RecyclerView que los datos han cambiado y que debe volver a cargar
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());     // NO borrar, ofrece informacion
            }
        });
    }
}
