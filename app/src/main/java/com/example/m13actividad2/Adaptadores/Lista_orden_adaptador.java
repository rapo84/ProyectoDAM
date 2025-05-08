package com.example.m13actividad2.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        this.listarProductos = procesarLista(listarProductos);
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        this.listarProductos = procesarLista(nuevaLista);
        notifyDataSetChanged();
    }

    private List<Producto> procesarLista(List<Producto> listaOriginal) {
        Map<String, Producto> productosUnicos = new HashMap<>();

        for (Producto producto : listaOriginal) {
            String codigo = producto.getCodigo();
            if (productosUnicos.containsKey(codigo)) {
                Producto productoExistente = productosUnicos.get(codigo);
                productoExistente.setCantidad(productoExistente.getCantidad() + 1);
            } else {
                producto.setCantidad(1);
                productosUnicos.put(codigo, producto);
            }
        }

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
        holder.tvCantidad.setText(String.valueOf(p.getCantidad()));
        holder.tvNombre.setText(p.getNombre());
        holder.tvPrecio.setText(String.valueOf(p.getPrecio()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(p, holder.getAdapterPosition());
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

    public void mostrarDialogoConfirmacion(Context context, Producto producto, int posicion, List<Producto> productos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ELIMINAR PRODUCTO");
        builder.setMessage("Estas seguro que deseas borrar el producto de la lista?")
                .setCancelable(false)
                .setPositiveButton("Aceptar", (dialog, id) -> {
                    eliminarProducto(producto, posicion);
                    productos.clear();
                    productos.addAll(getListarProductos());
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }
}
