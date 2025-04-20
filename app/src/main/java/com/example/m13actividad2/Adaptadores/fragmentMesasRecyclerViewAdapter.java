package com.example.m13actividad2.Adaptadores;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.renderscript.ScriptGroup;
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

    public fragmentMesasRecyclerViewAdapter(List<Producto> items) {
        productos = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = productos.get(position);
        //holder.nombre.setText(productos.get(position).getNombre().toString());
        holder.nombre.setText(productos.get(position) != null ? productos.get(position).getNombre() : "Nombre no disponible");
        // Crear un borde
        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.RECTANGLE);
        border.setColor(Color.parseColor("#070090")); // ✅ Color de fondo azul oscuro
        border.setStroke(4, Color.WHITE);             // ✅ Borde blanco de grosor 4
        holder.itemView.setBackground(border);        // Aplicar el fondo al item

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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }
    }
}