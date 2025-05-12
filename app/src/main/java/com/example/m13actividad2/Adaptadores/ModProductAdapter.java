package com.example.m13actividad2.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Actividades.ModificarProductos;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R; // Asegúrate de importar tu R correctamente
import com.example.m13actividad2.utils.Utilidad;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ModProductAdapter extends RecyclerView.Adapter<ModProductAdapter.ViewHolder> {
    private List<Producto> productos;
    private Context context;

    public ModProductAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_modificar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.tvNombre.setText(Html.fromHtml("<b>NOMBRE:</b> " + producto.getNombre(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvCategoria.setText(Html.fromHtml("<b>CATEGORIA:</b>: "+producto.getCategoria(), Html.FROM_HTML_MODE_LEGACY));
        holder.tvPrecio.setText(Html.fromHtml(String.format("<b>PRECIO:</b>: %.2f €", producto.getPrecio()), Html.FROM_HTML_MODE_LEGACY));
        holder.tvStock.setText(Html.fromHtml(String.format("<b>STOCK:</b>: %d unidades", producto.getCantidad()), Html.FROM_HTML_MODE_LEGACY));
        holder.tvCodigo.setText(Html.fromHtml("<b>CODIGO:</b>:: " + producto.getCodigo(), Html.FROM_HTML_MODE_LEGACY));

        // Aquí puedes poner los listeners para los botones
        holder.btnEliminar.setOnClickListener(v -> {
            //lamamos al metodo que mostrara el mensaje de confirmacion antes de eliminar definitivamente a un emmpleado de la BBDD
            mostrarDialogoConfirmacion("Estas seguro de querer eliminar este Producto?", context, producto, position);

        });

        holder.btnModificar.setOnClickListener(view -> {
            Intent intent = new Intent(context, ModificarProductos.class);
            intent.putExtra("productoSeleccionado",productos.get(position));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria, tvPrecio, tvStock, tvCodigo;
        Button btnEliminar, btnModificar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre_gestProduct);
            tvCategoria = itemView.findViewById(R.id.tvCategoria_gestProduct);
            tvPrecio = itemView.findViewById(R.id.tvprecio_gestProduct);
            tvStock = itemView.findViewById(R.id.tvStock_gestProduct);
            btnEliminar = itemView.findViewById(R.id.button);
            btnModificar = itemView.findViewById(R.id.button2);
            tvCodigo = itemView.findViewById(R.id.tvCodigo_gestProduct);
        }
    }

    public void mostrarDialogoConfirmacion(String mensaje, Context context, Producto producto, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // 'this' es el contexto de la actividad
        builder.setMessage(mensaje)
                .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes realizar la acción que quieres que ocurra al aceptar
                        eliminarproducto(producto, position);
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

    // Método para eliminar productos de Firebase
    private void eliminarproducto(Producto producto, int position) {
        String nombrelocal= Utilidad.recupernombrelocal(context);
        Log.i("⚠️ VER INFO PRODUCTO", "NOMBRE: " + producto.getNombre() + ", Codigo: " + producto.getCodigo());   // ESTE LOG ES SOLO PARA VER SI SE ESTA LEYENDO EL producto CORRECTAMENTE

        // priemro obtenemos la referencia en la bbdd del Producto que queremos eliminar
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales").child(nombrelocal).child("Productos")
                .child(producto.getCategoria()) // "para poder entrar en el nodo de la categoria donde se encuentre el producto"
                .child(producto.getCodigo()); // Se usa el codigo como identificador único de cada producto

        //luego de tener la referencia procedemos a eliminarlo y notificamos al adaptador que hay cambios
        databaseReference.removeValue().addOnSuccessListener(aVoid -> {     // aqui creamos un listener que cuando se borre el emmpleado de la bbdd nos permita ahora proceder a borrarlo de la lista del adaptador
            // y aqui eliminamos el elemento de la lista y notificamos al adaptador
            productos.remove(position);
            notifyItemRemoved(position);        // notifica al adaptador que hay cambios
            notifyItemRangeChanged(position, productos.size());
            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Error al eliminar el Producto", Toast.LENGTH_SHORT).show();
        });

    }
}

