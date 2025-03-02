package com.example.m13actividad2.Adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Modelos.Empleado;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.R;

import java.util.List;

public class EmpleadoAdapter extends RecyclerView.Adapter<EmpleadoAdapter.ViewHolder> {
    // lista para manejar los empleados que se lean de la BBDD
    private List<Persona> empleados;

    //pasamos el contexto de la actividad donde se llame al adaptadom (util cuando usamos toast o parecidos dentro del adapter)
    private Context context;

    // Constructor
    public EmpleadoAdapter(List<Persona> empleados, Context context) {
        this.empleados = empleados;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_empleado, parent, false);  // usamos el inflater con el contexto de la actividad donde se llame el adaptador y lo guardamos en la variable view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // indicamos que se va a mostrar en los componentes de cada uno de los elementos del Rcview
        Persona empleado = empleados.get(position);

        // Configurar datos en las vistas    AQUI LA ADVERTENCIA ES QUE NO ES EL FORMATO ADECUADO SI SE DESEA LUEGO TRADUCIR LA APP AUTOMATICAMENTE
        holder.tvNombre.setText("NOMBRE: " + empleado.getNombre() + " " + empleado.getApellidos());
        holder.tvCategoria.setText( "CATEGORIA: " + empleado.getCategoria());
        holder.tvEmail.setText("Email: " + empleado.getCorreo());
        holder.tvDni.setText("DNI: " + empleado.getDni());
        holder.sphorario.setText("HORARIO: " + empleado.getHorario());
    }

    @Override
    public int getItemCount() {
        return empleados.size();
    }

    // Clase interna para el ViewHolder aqui asociamos cada elemento del xml del item con una variable que es la que usaremos luego en el onBindViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria, tvEmail, tvDni, sphorario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDni = itemView.findViewById(R.id.tvDni);
            sphorario = itemView.findViewById(R.id.tvHorarios);
        }
    }
}

