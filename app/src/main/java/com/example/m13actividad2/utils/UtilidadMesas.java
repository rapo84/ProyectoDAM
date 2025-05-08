package com.example.m13actividad2.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.ListaFinalAdaptador;
import com.example.m13actividad2.ConextionsPrint.ImpresionBt;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.interfaces.ProductosCargadosListener;
import com.example.m13actividad2.interfaces.RoleCheckCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilidadMesas {

    public static void obtenercategorias(Context context, List<String> lista, RecyclerView.Adapter adapter) {
        String nombrelocal = Utilidad.recupernombrelocal(context);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales").child(nombrelocal).child("Productos");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lista.clear();
                for (DataSnapshot categoriaSnapshot : snapshot.getChildren()) {
                    String nombreCategoria = categoriaSnapshot.getKey();
                    lista.add(nombreCategoria);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());
            }
        });
    }

    public static void cargarProductosPorCategoria(Context context, List<Producto> productos, RecyclerView.Adapter adapter, String categoriaSeleccionada) {
        String nombrelocal = Utilidad.recupernombrelocal(context);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales").child(nombrelocal).child("Productos").child(categoriaSeleccionada);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productos.clear();
                for (DataSnapshot categoriaSnapshot : snapshot.getChildren()) {
                    String nombre = categoriaSnapshot.child("nombre").getValue(String.class);
                    String categoria = categoriaSnapshot.child("categoria").getValue(String.class);
                    String codigo = categoriaSnapshot.child("codigo").getValue(String.class);
                    Integer cantidad = categoriaSnapshot.child("cantidad").getValue(Integer.class);
                    if (cantidad == null) {
                        cantidad = 0;
                    }
                    Double precio = categoriaSnapshot.child("precio").getValue(Double.class);
                    if (precio == null) {
                        precio = 0.0;
                    }

                    productos.add(new Producto(codigo, nombre, precio, categoria, cantidad));
                    Log.d("Producto", "✅ nombre: " + nombre + ", categoria: " + categoria + ", codigo: " + codigo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());
            }
        });
    }

    public static void cargarHistorialdeProductosDeMesas(Context context, String mesaSeleccionada, ProductosCargadosListener listener) {
        String nombrelocal = Utilidad.recupernombrelocal(context);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales")
                .child(nombrelocal)
                .child("Mesas")
                .child(mesaSeleccionada);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Producto> productos = new ArrayList<>();

                for (DataSnapshot ProductosMesasSnapshot : snapshot.getChildren()) {
                    String nombre = ProductosMesasSnapshot.child("nombre").getValue(String.class);
                    String categoria = ProductosMesasSnapshot.child("categoria").getValue(String.class);
                    String codigo = ProductosMesasSnapshot.child("codigo").getValue(String.class);
                    Integer cantidad = ProductosMesasSnapshot.child("cantidad").getValue(Integer.class);
                    Double precio = ProductosMesasSnapshot.child("precio").getValue(Double.class);

                    if (cantidad == null) cantidad = 0;
                    if (precio == null) precio = 0.0;

                    productos.add(new Producto(codigo, nombre, precio, categoria, cantidad));
                }

                if (listener != null) {
                    listener.callbackProductosCargados(productos);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());
                if (listener != null) {
                    listener.callbackProductosCargados(new ArrayList<>());
                }
            }
        });
    }

    public static void guardarPedidosEnElHistorial(Context context, List<Producto> productosNuevos, String mesaSeleccionada) {
        String nombrelocal = Utilidad.recupernombrelocal(context);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales")
                .child(nombrelocal)
                .child("Mesas")
                .child(mesaSeleccionada);

        cargarHistorialdeProductosDeMesas(context, mesaSeleccionada, productosExistentes -> {
            Map<String, Producto> mapaExistente = new HashMap<>();
            for (Producto producto : productosExistentes) {
                if (producto.getCodigo() != null) {
                    mapaExistente.put(producto.getCodigo(), producto);
                }
            }

            for (Producto nuevo : productosNuevos) {
                if (nuevo.getCodigo() == null) continue;

                Producto existente = mapaExistente.get(nuevo.getCodigo());

                if (existente != null) {
                    int nuevaCantidad = existente.getCantidad() + nuevo.getCantidad();
                    nuevo.setCantidad(nuevaCantidad);
                }

                databaseReference.child(nuevo.getCodigo()).setValue(nuevo);
            }
        });
    }

    public static void mostrarDialogoConfirmacionLiberarmesa(String mensaje, Context context, String mesaseleccionada, List<Producto> lista, Double totalFormateado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("⚠️⚠️ ATENCION ⚠️⚠️");
        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Aceptar", (dialog, id) -> {
                    LiberarMesa(context, mesaseleccionada, lista, totalFormateado);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void LiberarMesa(Context context, String mesaseleccionada, List<Producto> lista, Double totalFormateado) {
        String nombrelocal = Utilidad.recupernombrelocal(context);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales").child(nombrelocal).child("Mesas");

        Utilidad.checkUserRole(context, "Admin", nombrelocal, new RoleCheckCallback() {
            @Override
            public void onRoleChecked(boolean tieneElRol) {
                if (tieneElRol) {
                    UtilidadMesas.actualizarInventario(context, lista);
                    databaseReference.child(mesaseleccionada).removeValue().addOnSuccessListener(aVoid -> {
                        ImpresionBt.Imprimir(context, lista, totalFormateado, mesaseleccionada);
                        Toast.makeText(context, "Mesa liberada", Toast.LENGTH_SHORT).show();
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al intentar liberar la mesa", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("PERMISOS INSUFICIENTES");
                    builder.setMessage("Solo el administrador puede liberar las mesas")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss());
                    builder.create().show();
                }
            }

            @Override
            public void onRoleReceived(String role, String localex) {}
        });
    }

    public static void actualizarInventario(Context context, List<Producto> productosFinalesMesa) {
        String nombreLocal = Utilidad.recupernombrelocal(context);
        boolean msg = false;
        try {
            for (Producto producto : productosFinalesMesa) {
                String codigo = producto.getCodigo();
                String categoria = producto.getCategoria();
                int cantidadRestar = producto.getCantidad();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Locales").child(nombreLocal)
                        .child("Productos").child(categoria).child(codigo).child("cantidad");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int cantidadinventario = snapshot.getValue(Integer.class);
                        int nuevaCantidad = cantidadinventario - cantidadRestar;
                        if (nuevaCantidad < 0) {
                            databaseReference.setValue(0);
                        } else {
                            databaseReference.setValue(nuevaCantidad)
                                    .addOnFailureListener(e -> {
                                        Log.e("Inventario", "Error al actualizar cantidad: " + e.getMessage());
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Inventario", "Error al leer inventario: " + error.getMessage());
                    }
                });
            }
            if (msg) {
                Toast.makeText(context, "Ha ocurrido un error al actualizar el inventario", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error al actualizar inventario", Toast.LENGTH_SHORT).show();
        }
    }

}
