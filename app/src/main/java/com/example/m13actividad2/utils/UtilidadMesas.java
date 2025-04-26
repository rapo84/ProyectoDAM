package com.example.m13actividad2.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

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
        String nombrelocal = Utilidad.recupernombrelocal(context);           //Obtenemos el nombre del local a donde pertenece el dispositivo

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
        String nombrelocal = Utilidad.recupernombrelocal(context);           //Obtenemos el nombre del local a donde pertenece el dispositivo

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

                // Llamar al callback
                if (listener != null) {
                    listener.callbackProductosCargados(productos);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "❌ Error al leer datos", databaseError.toException());
                if (listener != null) {
                    listener.callbackProductosCargados(new ArrayList<>()); // Retornar lista vacía en caso de error
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

        cargarHistorialdeProductosDeMesas(context, mesaSeleccionada, productosExistentes -> { //aqui usamos el callback pero en lambda para simplificar lo cual lo que hacemos es colocar loq ue devolveria el callback en una variable
            // Mapeamos los productos existentes por su código para fácil acceso
            Map<String, Producto> mapaExistente = new HashMap<>();
            for (Producto producto : productosExistentes) {
                if (producto.getCodigo() != null) {
                    mapaExistente.put(producto.getCodigo(), producto);
                }
            }

            // Recorremos los nuevos productos para sumarlos o agregarlos
            for (Producto nuevo : productosNuevos) {
                if (nuevo.getCodigo() == null) continue;

                Producto existente = mapaExistente.get(nuevo.getCodigo());

                if (existente != null) {
                    // Ya existe -> sumamos cantidades
                    int nuevaCantidad = existente.getCantidad() + nuevo.getCantidad();
                    nuevo.setCantidad(nuevaCantidad);
                }

                // Guardamos (si no existía lo crea, si ya existía lo sobrescribe con la cantidad sumada)
                databaseReference.child(nuevo.getCodigo()).setValue(nuevo);
            }
        });
    }

    public static void mostrarDialogoConfirmacionLiberarmesa(String mensaje, Context context, String mesaseleccionada, List<Producto> lista, Double totalFormateado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // es el contexto de la actividad
        builder.setTitle("⚠️⚠️ ATENCION ⚠️⚠️");
        builder.setMessage(mensaje)
                .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes realizar la acción que quieres que ocurra al aceptar
                        LiberarMesa(context,mesaseleccionada,lista,totalFormateado);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cierra el diálogo si se selecciona "Cancelar"
                    }
                });

        AlertDialog alert = builder.create();
        alert.show(); // Muestra el diálogo
    }

    // metodo para borrar la mesa seleccionada
    public static void LiberarMesa(Context context, String mesaseleccionada, List<Producto> lista, Double totalFormateado) {
        String nombrelocal = Utilidad.recupernombrelocal(context);
        // Obtenemos la referencia al producto
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales").child(nombrelocal).child("Mesas");

        Utilidad.checkUserRole(context, "Admin", nombrelocal, new RoleCheckCallback() {
            @Override
            public void onRoleChecked(boolean tieneElRol) {
                if (tieneElRol) {
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(context); // es el contexto de la actividad
                    builder.setTitle("PERMISOS INSUFICIENTES");
                    builder.setMessage("Solo el administrador puede liberar las mesas")
                            .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss(); // Cierra el diálogo si se selecciona "Cancelar"
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show(); // Muestra el diálogo
                }
            }

            /*  este metodo solo existe porque quise usar una sola interfaz para las 2 respuestas que necesitaba del rolecheckcallback
                pero en este metodo aqui no pinta nada! pero por la interfaz debo implementarlo obligatoriamente*/
            @Override
            public void onRoleReceived(String role, String localex) {
            }
        });
    }

    public static void actualizarInventario(Context context, List<Producto> productos) {
        String nombreLocal = Utilidad.recupernombrelocal(context);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales").child(nombreLocal).child("Productos");

        try {
            for (Producto producto : productos) {
                databaseReference.child(producto.getCodigo())
                        .removeValue()
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Error al borrar producto: " + producto.getNombre(), Toast.LENGTH_SHORT).show()
                        );
            }
            Toast.makeText(context, "Productos eliminados del inventario", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error al actualizar inventario", Toast.LENGTH_SHORT).show();
        }
    }




}