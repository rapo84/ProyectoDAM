package com.example.m13actividad2.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m13actividad2.Modelos.Persona;
import com.example.m13actividad2.R;
import com.example.m13actividad2.firebaseServerSdk.ApiService;
import com.example.m13actividad2.firebaseServerSdk.RetrofitClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadoAdapterBorrar extends RecyclerView.Adapter<EmpleadoAdapterBorrar.ViewHolder>{
    // lista para manejar los empleados que se lean de la BBDD
    private List<Persona> empleados;

    //pasamos el contexto de la actividad donde se llame al adaptadom (util cuando usamos toast o parecidos dentro del adapter)
    private Context context;

    // Constructor
    public EmpleadoAdapterBorrar(List<Persona> empleados, Context context) {
        this.empleados = empleados;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_empleado_borrar, parent, false); // usamos el inflater con el contexto de la actividad donde se llame el adaptador y lo guardamos en la variable view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // indicamos que se va a mostrar en los componentes de cada uno de los elementos del Rcview
        Persona empleado = empleados.get(position);

        // Configurar datos en las vistas
        holder.tvNombre.setText("NOMBRE: " + empleado.getNombre() + " " + empleado.getApellidos());
        holder.tvCategoria.setText("CATEGORIA: " + empleado.getCategoria());
        holder.tvEmail.setText("Email: " + empleado.getCorreo());
        holder.tvDni.setText("DNI: " + empleado.getDni());


        // Evento de clic en el botón de eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            //lamamos al metodo que mostrara el mensaje de confirmacion antes de eliminar definitivamente a un emmpleado de la BBDD
            mostrarDialogoConfirmacion("Estas seguro de querer eliminar a este empleado?", context, empleado, position);

        });
    }

    @Override
    public int getItemCount() {
        return empleados.size();
    }


    // Clase interna para el ViewHolder aqui asociamos cada elemento del xml del item con una variable que es la que usaremos luego en el onBindViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria, tvEmail, tvDni;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDni = itemView.findViewById(R.id.tvDni);
            btnEliminar = itemView.findViewById(R.id.btn_Eliminar_en_lista);
        }
    }


    /*   mostramos mensaje para verificar que realmente quiera borrar al usuaraio y si acepta se llama al metodo para eliminar el usuario de la bbdd y este llama metodo para elimianr de auth  */
    public void mostrarDialogoConfirmacion(String mensaje, Context context, Persona empleado, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // 'this' es el contexto de la actividad
        builder.setMessage(mensaje)
                .setCancelable(false) // El diálogo no se puede cancelar tocando fuera de él
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí puedes realizar la acción que quieres que ocurra al aceptar
                        eliminarEmpleado(empleado, position);
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



    // Método para eliminar empleado de Firebase y llama a metodo para eliminar el empleado tambien del AUTH  modifiacar cuando agreemos el local
    private void eliminarEmpleado(Persona empleado, int position) {
        String nombrelocal= Utilidad.recupernombrelocal(context);
        //Log.e("VER INFO PERSONA", "NOMBRE: " + empleado.getNombre() + ", UID: " + empleado.getUid());    ESTE LOG ES SOLO PARA VER SI SE ESTA LEYENDO EL EMPLEADO CORRECTAMENTE

        // priemro obtenemos la referencia en la bbdd del empleado que queremos eliminar
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Locales").child(nombrelocal).child("Empleados")
                .child(empleado.getCategoria()) // "Admin" o "Empleado"
                .child(empleado.getDni()); // Se usa el DNI como identificador único

        //luego de tener la referencia procedemos a eliminarlo y notificamos al adaptador que hay cambios
        databaseReference.removeValue().addOnSuccessListener(aVoid -> {     // aqui creamos un listener que cuando se borre el emmpleado de la bbdd nos permita ahora proceder a borrarlo de la lista del adaptador
            // y aqui eliminamos el elemento de la lista y notificamos al adaptador
            empleados.remove(position);
            notifyItemRemoved(position);        // notifica al adaptador que hay cambios
            Toast.makeText(context, "Empleado eliminado", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
        });

        eliminarUsuariodeAuth(empleado.getUid());  // ya habiendose eliminado de la bbdd y de la lista ahora procedemos a ubicar al usuario que tenia asigand en el firebase auth y lo eliminamos

    }


    // eliminamos el usuario que se registro en el firebase auth por medio de peticion al servidor ya que no se puede hacer directamente por la app por seguridad
    private void eliminarUsuariodeAuth(String uid) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.eliminarUsuario(uid);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("EliminarUsuario", "✅ Usuario eliminado con éxito");
                } else {
                    Log.e("EliminarUsuario", "❌ Error al eliminar el usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EliminarUsuario", "❌ Fallo en la llamada: " + t.getMessage());
            }
        });
    }



}
