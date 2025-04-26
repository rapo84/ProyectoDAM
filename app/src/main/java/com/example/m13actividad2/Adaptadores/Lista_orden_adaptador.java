package com.example.m13actividad2.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lista_orden_adaptador extends RecyclerView.Adapter<Lista_orden_adaptador.ListaOrderViewHolder> {
    private List<Producto> listarProductos;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Producto producto, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public Lista_orden_adaptador(List<Producto> listarProductos) {
        // Procesamos la lista inicial para eliminar duplicados y contar las cantidades
        this.listarProductos = procesarLista(listarProductos);
    }


    public void actualizarLista(List<Producto> nuevaLista) {
        // Procesamos la nueva lista para eliminar duplicados y contar las cantidades
        this.listarProductos = procesarLista(nuevaLista);
        notifyDataSetChanged();
    }

    // Método para procesar la lista y eliminar duplicados
    private List<Producto> procesarLista(List<Producto> listaOriginal) {
        // Utilizamos un HashMap para contar las cantidades de cada producto
        Map<String, Producto> productosUnicos = new HashMap<>();

        // Recorremos la lista original
        for (Producto producto : listaOriginal) {
            String codigo = producto.getCodigo();
            if (productosUnicos.containsKey(codigo)) {
                // Si el producto ya existe, aumentamos su cantidad
                Producto productoExistente = productosUnicos.get(codigo);
                productoExistente.setCantidad(productoExistente.getCantidad() + 1);
            } else {
                // Si el producto es nuevo, lo agregamos con cantidad 1
                producto.setCantidad(1);
                productosUnicos.put(codigo, producto);
            }
        }

        // Convertimos el HashMap a una lista
        return new ArrayList<>(productosUnicos.values());
    }

    @NonNull
    @Override
    public ListaOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lista_ordenes, parent, false);
        return new ListaOrderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaOrderViewHolder holder, int position) {
        Producto p = listarProductos.get(position);

        // Mostramos la cantidad del producto
        holder.tvCantidad.setText(String.valueOf(p.getCantidad()));
        holder.tvNombre.setText(p.getNombre());
        holder.tvPrecio.setText(String.valueOf(p.getPrecio()));

        // Configurar el click listener para cada item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(p, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listarProductos.size();
    }

    public class ListaOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCantidad, tvNombre, tvPrecio;

        public ListaOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCantidad = itemView.findViewById(R.id.tv_lista_order_cantidad);
            tvNombre = itemView.findViewById(R.id.tv_lista_order_nombre_producto);
            tvPrecio = itemView.findViewById(R.id.tv_lista_order_precio);
        }
    }

    public List<Producto> getListarProductos() {

        return listarProductos;
    }

    public void setListaProductos(List<Producto> nuevaLista) {
        this.listarProductos = nuevaLista;
        notifyDataSetChanged();
    }

    // Método para eliminar un producto de la lista
    public void eliminarProducto(Producto producto, int position) {
        if (position >= 0 && position < listarProductos.size()) {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
                notifyItemChanged(position);
            } else {
                listarProductos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listarProductos.size());
            }
        } else {
            Log.e("Adaptador", "❌ Posición inválida al intentar eliminar producto: " + position);
        }
    }

    /*   mostramos mensaje para verificar que realmente se quiera eliminar el articulo*/
    public void mostrarDialogoConfirmacion(Context context, Producto producto, int posicion, List<Producto> productos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // 'this' es el contexto de la actividad
        builder.setTitle("ELIMINAR PRODUCTO");
        builder.setMessage("Estas seguro que deseas borrar el producto de la lista?")
                .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes realizar la acción que quieres que ocurra al aceptar
                        eliminarProducto(producto, posicion);

                        // 🔁 Actualiza también la lista original que se usa para guardar
                        productos.clear();
                        productos.addAll(getListarProductos());
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
}
