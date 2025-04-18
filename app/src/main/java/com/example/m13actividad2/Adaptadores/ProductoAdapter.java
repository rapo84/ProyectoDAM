package com.example.m13actividad2.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;

import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<Producto> productos;

    public ProductoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto p = productos.get(position);

        // Log para ver si realmente se está “binding”
        Log.d("ProductoAdapter", "onBindViewHolder pos=" + position + " → " + p.getNombre());

        holder.tvCategoria.setText(p.getCategoria());
        holder.tvNombre.setText(p.getNombre());
        holder.tvPrecio.setText(String.format(Locale.getDefault(),
                "Precio: %.2f €", p.getPrecio()));
        holder.tvDescripcion.setText(p.getDescripcion() != null
                ? p.getDescripcion() : "");
    }

    @Override
    public int getItemCount() {
        int size = productos == null ? 0 : productos.size();
        Log.d("ProductoAdapter", "getItemCount() = " + size);
        return size;
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoria, tvNombre, tvPrecio, tvDescripcion;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoria   = itemView.findViewById(R.id.tvCategoria);
            tvNombre      = itemView.findViewById(R.id.tvNombre);
            tvPrecio      = itemView.findViewById(R.id.tvPrecio);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
