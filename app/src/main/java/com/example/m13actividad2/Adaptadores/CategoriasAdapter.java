package com.example.m13actividad2.Adaptadores;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.R;

import java.util.List;


public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriasViewHolder> {

    private List<String> categorias;
    private int[] colores = {R.color.blue, R.color.green, R.color.dark_red, R.color.browm, R.color.fondonegro, R.color.gray };
    private OnCategoriaClickListener listener;

    public CategoriasAdapter(List<String> categorias, OnCategoriaClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriasAdapter.CategoriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rvcategorias_mesas, parent, false);
        return new CategoriasAdapter.CategoriasViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasAdapter.CategoriasViewHolder holder, int position) {
        String cat = categorias.get(position);
        holder.tvcategorias.setText(cat);
        holder.itemView.setBackgroundColor(colores[position % colores.length]);

        // Crear un borde
        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.RECTANGLE);
        border.setColor(colores[position % colores.length]);  // Color de fondo
        border.setStroke(4, Color.BLACK);  // Grosor y color del borde (se puedes modificar)
        // Aplicar el borde al itemView
        holder.itemView.setBackground(border);


        // Detectar clic
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoriaClick(cat);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }
    public static class CategoriasViewHolder extends RecyclerView.ViewHolder {
        TextView tvcategorias;

        public CategoriasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcategorias = itemView.findViewById(R.id.tv_item_categoria_mesas);
        }
    }
    public interface OnCategoriaClickListener {
        void onCategoriaClick(String categoria);
    }
}
