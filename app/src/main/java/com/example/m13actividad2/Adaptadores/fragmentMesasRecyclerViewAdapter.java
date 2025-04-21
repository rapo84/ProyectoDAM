package com.example.m13actividad2.Adaptadores;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.databinding.FragmentItemBinding;

import java.util.List;


public class fragmentMesasRecyclerViewAdapter extends RecyclerView.Adapter<fragmentMesasRecyclerViewAdapter.ViewHolder> {

    private final List<Producto> productos;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onProductoSeleccionado(Producto producto);
    }

    public fragmentMesasRecyclerViewAdapter(List<Producto> productos, OnProductoClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.bind(producto);

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nombre;
        public Producto mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            nombre = binding.itemName;

            // Manejamos el click desde aquí
            itemView.setOnClickListener(v -> {
                if (listener != null && mItem != null) {
                    listener.onProductoSeleccionado(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

        public void bind(Producto producto) {
            mItem = producto;
            nombre.setText(producto != null ? producto.getNombre() : "Nombre no disponible");

            GradientDrawable border = new GradientDrawable();
            border.setShape(GradientDrawable.RECTANGLE);
            border.setColor(Color.parseColor("#070090")); // Color de fondo azul oscuro
            border.setStroke(4, Color.WHITE);             // Borde blanco
            itemView.setBackground(border);
        }
    }


}