package com.example.m13actividad2.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.interfaces.RoleCheckCallback;
import com.example.m13actividad2.utils.Utilidad;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ListaFinalAdaptador extends RecyclerView.Adapter<ListaFinalAdaptador.ListaFinalViewHolder>{
    private List<Producto> historialOrdenes;
    private Context context;
    private String mesaSeleccionada;

    public ListaFinalAdaptador(List<Producto> historialOrdenes, Context context, String mesaSeleccionada) {
        this.historialOrdenes = historialOrdenes;
        this.context = context;
        this.mesaSeleccionada = mesaSeleccionada;
    }

    @NonNull
    @Override
    public ListaFinalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_ordenes_borrables, parent, false);
        return new ListaFinalViewHolder(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull ListaFinalAdaptador.ListaFinalViewHolder holder, int position) {
        Producto p = historialOrdenes.get(position);

        holder.tvCantidad.setText(String.valueOf(p.getCantidad()));
        holder.tvNombre.setText(p.getNombre());
        holder.tvPrecio.setText(String.valueOf(p.getPrecio()));

        // Evento de clic en el botón de eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            //lamamos al metodo que mostrara el mensaje de confirmacion antes de eliminar definitivamente a un emmpleado de la BBDD
            mostrarDialogoConfirmacion("Estas seguro de querer eliminar este producto?", context, p, position, mesaSeleccionada);

        });

    }

    @Override
    public int getItemCount() {
        return historialOrdenes.size();
    }

    public class ListaFinalViewHolder extends RecyclerView.ViewHolder {
        TextView tvCantidad, tvNombre, tvPrecio;
        ImageButton btnEliminar;

        public ListaFinalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCantidad = itemView.findViewById(R.id.tv_lista_order_cantidad);
            tvNombre = itemView.findViewById(R.id.tv_lista_order_nombre_producto);
            tvPrecio = itemView.findViewById(R.id.tv_lista_order_precio);
            btnEliminar = itemView.findViewById(R.id.imgb_Lista_final_borrar);
        }
    }

    /*   mostramos mensaje para verificar que realmente quiera borrar el producto y si acepta se llama al metodo para eliminar el producto de la bbdd*/
    public void mostrarDialogoConfirmacion(String mensaje, Context context, Producto producto, int position, String mesaseleccionada) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // es el contexto de la actividad
        builder.setMessage(mensaje)
                .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes realizar la acción que quieres que ocurra al aceptar
                        eliminarProducto(producto, position, mesaseleccionada);
                        notifyDataSetChanged();
                        //falta eliminar el usuario del login
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



    // Método para eliminar empleado de Firebase y llama a metodo para eliminar el empleado tambien del AUTH  modifiacar cuando agreemos el local
    private void eliminarProducto(Producto producto, int position, String mesaseleccionada) {
        String nombrelocal= Utilidad.recupernombrelocal(context);
        //Log.e("VER INFO Producto", "NOMBRE: " + producto.getNombre() + ", UID: " + producto.getCodigo());    ESTE LOG ES SOLO PARA VER SI SE ESTA LEYENDO EL producto CORRECTAMENTE

        // Obtenemos la referencia al producto
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales").child(nombrelocal).child("Mesas")
                .child(mesaseleccionada)
                .child(producto.getCodigo());

        Utilidad.checkUserRole(context, "Admin", nombrelocal, new RoleCheckCallback() {
            @Override
            public void onRoleChecked(boolean tieneElRol) {
                if (tieneElRol) {
                    if (producto.getCantidad() > 1) {
                        // Caso: cantidad mayor que 1 → restamos 1 y actualizamos
                        int nuevaCantidad = producto.getCantidad() - 1;
                        databaseReference.child("cantidad").setValue(nuevaCantidad)
                                .addOnSuccessListener(aVoid -> {
                                    producto.setCantidad(nuevaCantidad); // también lo actualizamos localmente
                                    notifyItemChanged(position); // refrescamos ese item en la lista
                                    Toast.makeText(context, "Cantidad actualizada (−1)", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Error al actualizar cantidad", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Si la cantidad es 1, se elimina el producto
                        databaseReference.removeValue().addOnSuccessListener(aVoid -> {
                            // Buscar el índice por código y eliminar de la lista
                            int index = -1;
                            for (int i = 0; i < historialOrdenes.size(); i++) {
                                if (historialOrdenes.get(i).getCodigo().equals(producto.getCodigo())) {
                                    index = i;
                                    break;
                                }
                            }

                            if (index != -1) {
                                historialOrdenes.remove(index);
                                notifyItemRemoved(index);  //aunque notificamos que eliminamos un elemento para que se refresque la imagen correctamene usamos...
                                notifyDataSetChanged();  // si no no se refresca la imagen correctamente
                                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Adaptador", "❌ Producto no encontrado en la lista para eliminar.");
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context); // es el contexto de la actividad
                    builder.setTitle("PERMISOS INSUFICIENTES");
                    builder.setMessage("Solo el administrador puede eliminar productos")
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
                pero en este metodo "checkUserRoleParaSaberSiTienePermisos" no funciona para nada pero al ser interfaz debo implementarlo obligatoriamente*/
            @Override
            public void onRoleReceived(String role, String localex) {
            }
        });






    }


}
