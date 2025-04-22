package com.example.m13actividad2.Adaptadores;

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

public class Lista_orden_adaptador extends RecyclerView.Adapter<Lista_orden_adaptador.ListaOrderViewHolder>{
    private List<Producto> ListarProductos;

    public Lista_orden_adaptador(List<Producto> listarProductos) {
        // Procesamos la lista para eliminar duplicados y contar las cantidades
        this.ListarProductos = procesarLista(listarProductos);
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
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lista_ordenes,parent,false);
        return new ListaOrderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Lista_orden_adaptador.ListaOrderViewHolder holder, int position) {
        Producto p = ListarProductos.get(position);

        // Mostramos la cantidad del producto
        holder.tvCantidad.setText(String.valueOf(p.getCantidad()));
        holder.tvNombre.setText(p.getNombre());
        holder.tvPrecio.setText(String.valueOf(p.getPrecio()));

    }

    @Override
    public int getItemCount() {
        return ListarProductos.size();
    }

    public static class ListaOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCantidad, tvNombre, tvPrecio;


        public ListaOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCantidad = itemView.findViewById(R.id.tv_lista_order_cantidad);
            tvNombre = itemView.findViewById(R.id.tv_lista_order_nombre_producto);
            tvPrecio = itemView.findViewById(R.id.tv_lista_order_precio);
        }
    }
    public void actualizarLista(List<Producto> nuevaLista) {
        // Procesamos la nueva lista para eliminar duplicados y contar las cantidades
        this.ListarProductos = procesarLista(nuevaLista);
        notifyDataSetChanged();
    }

    public List<Producto> getListarProductos() {
        return ListarProductos;
    }
}
