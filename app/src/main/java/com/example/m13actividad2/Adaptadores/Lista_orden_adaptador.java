package com.example.m13actividad2.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;

import java.util.List;

public class Lista_orden_adaptador extends RecyclerView.Adapter<Lista_orden_adaptador.ListaOrderViewHolder>{
    private List<Producto> ListarProductos;

    public Lista_orden_adaptador(List<Producto> listarProductos) {
        this.ListarProductos = listarProductos;
    }

    @NonNull
    @Override
    public Lista_orden_adaptador.ListaOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lista_ordenes,parent,false);
        return new ListaOrderViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Lista_orden_adaptador.ListaOrderViewHolder holder, int position) {
        Producto p = ListarProductos.get(position);
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
        this.ListarProductos = nuevaLista;
        notifyDataSetChanged(); // <- esto es lo importante
    }
}
