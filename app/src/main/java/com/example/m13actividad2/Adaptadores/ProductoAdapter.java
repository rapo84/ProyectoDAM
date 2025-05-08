package com.example.m13actividad2.Adaptadores;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;

import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productos;
    private Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.tvNombre.setText(Html.fromHtml("<b>NOMBRE:</b> " + producto.getNombre(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvCategoria.setText(Html.fromHtml("<b>CATEGORIA:</b>: "+producto.getCategoria(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvPrecio.setText(Html.fromHtml(String.format("<b>PRECIO:</b>: %.2f €", producto.getPrecio()), Html.FROM_HTML_MODE_LEGACY));
        holder.tvStock.setText(Html.fromHtml(String.format("<b>STOCK:</b>: %d unidades", producto.getCantidad()), Html.FROM_HTML_MODE_LEGACY));
        holder.tvCodigo.setText(Html.fromHtml("<b>CODIGO:</b>:: " + producto.getCodigo(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvdescripcion.setText(Html.fromHtml("<b>DESCRIPCION:</b>:: " + producto.getDescripcion(), Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        int size = productos == null ? 0 : productos.size();
        Log.d("ProductoAdapter", "getItemCount() = " + size);
        return size;
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria, tvPrecio, tvStock, tvCodigo, tvdescripcion;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre_listarProductos);
            tvCategoria = itemView.findViewById(R.id.tvCategoria_listarProductos);
            tvPrecio = itemView.findViewById(R.id.tvPrecio_listarProductos);
            tvStock = itemView.findViewById(R.id.tvStock_listarProductos);
            tvCodigo = itemView.findViewById(R.id.tvCodigo_listarProductos);
            tvdescripcion = itemView.findViewById(R.id.tvDescripcion_listarProductos);
        }
    }
}
