package com.example.m13actividad2.Actividades;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminActivityRegistrar extends AppCompatActivity {
    private EditText nombre, apellidos, dni, email, pswd, categoria;
    private Button registrar;
    Spinner spinnerCategoria, spinnerhorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nombre=findViewById(R.id.emp_nombre);
        apellidos=findViewById(R.id.emp_apellidos);
        dni=findViewById(R.id.emp_dni);
        email=findViewById(R.id.emp_correo);
        pswd=findViewById(R.id.emp_contraseña);
        spinnerCategoria=findViewById(R.id.spinner_categoria);
        spinnerhorario = findViewById(R.id.spinner_admin_horario);
        registrar=findViewById(R.id.bt_RegEmpleado);





        // llenamos los spiner
        String[] opciones = {"Admin", "Empleado"};
        String[] turnos = {"Mañana","Tarde","Partido"};

        //llenamos el spiner de categoria
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_personalizado, opciones);
        spinnerCategoria.setAdapter(adapter);

        //llenamos el spiner de horarios
        ArrayAdapter<String> adapterhorarios = new ArrayAdapter<>(this, R.layout.item_spinner_personalizado, turnos);
        spinnerhorario.setAdapter(adapterhorarios);

        registrar.setOnClickListener(view -> {
            String Nombre = nombre.getText().toString().trim();
            String Apellidos = apellidos.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Dni = dni.getText().toString().trim();
            String Pswd = pswd.getText().toString().trim();
            String Categoria = spinnerCategoria.getSelectedItem().toString();
            String horario = spinnerhorario.getSelectedItem().toString();

            if (Nombre.isEmpty() || Apellidos.isEmpty() || Email.isEmpty() || Dni.isEmpty() || Pswd.isEmpty() || Categoria.isEmpty()){
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

            Utilidad.RegUsuarioEnAtuh(this, Nombre, Apellidos, Email, Dni, Pswd, Categoria, horario);

            Intent intent = new Intent(AdminActivityRegistrar.this,OpcionesDeAdmin.class);
            startActivity(intent);
            finish();

        });

    }

}