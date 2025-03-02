package com.example.m13actividad2.Actividades;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.Adaptadores.Utilidad;
import com.example.m13actividad2.R;
import com.example.m13actividad2.firebaseServerSdk.LocalesCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class VentanaSoporte extends AppCompatActivity {
    private Button Addlocal, Addadmin, logout, enlazar;
    private EditText localname, adminName, adminLastname, dni, email, password;
    private String nombrelocal;
    private Spinner spinnerHorarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_soporte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Addlocal= findViewById(R.id.bt_soporte_addLocal);
        Addadmin= findViewById(R.id.bt_soporte_crearAdmin);
        logout= findViewById(R.id.bt_soporte_salir);
        enlazar= findViewById(R.id.bt_soporte_addLocal_interno);

        localname= findViewById(R.id.et_soporte_localname);
        adminName= findViewById(R.id.et_soporte_adminName);
        adminLastname= findViewById(R.id.et_soporte_adminApellido);
        dni= findViewById(R.id.et_soporte_adminDni);
        email= findViewById(R.id.et_soporte_adminEmail);
        password= findViewById(R.id.et_soporte_adminPassword);

        spinnerHorarios = findViewById(R.id.spinner_soporte_horario);

        nombrelocal= localname.getText().toString().trim();


        //llenmos el spinner
        String[] turnos = {"Mañana","Tarde","Partido"};
        //llenamos el spiner de horarios
        ArrayAdapter<String> adapterhorarios = new ArrayAdapter<>(this, R.layout.item_spinner_personalizado, turnos);
        spinnerHorarios.setAdapter(adapterhorarios);

        Addlocal.setOnClickListener(view -> {
            nombrelocal = localname.getText().toString().trim();

            Utilidad.ObtenerLocalesExistentes(new LocalesCallback() {
                @Override
                public void onCallback(ArrayList<String> listalocalesexistentes) {
                    // 📌 Ahora `VerificarExistlocal()` recibe la lista llena porque espera a que la consulta a firebase se complete antes de trabjar con una lista vacia
                    Utilidad.VerificarExistlocal(Addlocal.getContext(), nombrelocal, listalocalesexistentes);
                }
            });
        });

        enlazar.setOnClickListener(view -> {
            nombrelocal= localname.getText().toString().trim();
            Utilidad.guardarNombrenegocioLocalmente(this, nombrelocal);
        });

        Addadmin.setOnClickListener(view -> {
            String Nombre = adminName.getText().toString().trim();
            String Apellidos = adminLastname.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Dni = dni.getText().toString().trim();
            String Pswd = password.getText().toString().trim();
            String horario = spinnerHorarios.getSelectedItem().toString();
            String Categoria = "Admin";
            if (Nombre.isEmpty() || Apellidos.isEmpty() || Email.isEmpty() || Dni.isEmpty() || Pswd.isEmpty()){
                Toast.makeText(this, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Utilidad.patronDNI(Dni)) {
                Toast.makeText(this, "El DNI no tiene el formato correcto", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Utilidad.patronEmail(Email)) {
                Toast.makeText(this, "El email no tiene el formato correcto", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Utilidad.patronPswd(Pswd)) {
                Toast.makeText(this, "La contraseña no cumple los requisitos", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilidad.RegUsuarioEnAtuh(this,Nombre, Apellidos, Email, Dni, Pswd, Categoria, horario);
        });

        logout.setOnClickListener(view -> {
            Utilidad.cerrarSesionYRedirigir(this, Ventana_Inicial.class);
        });


    }

}