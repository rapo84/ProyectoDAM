package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.Adaptadores.Utilidad;
import com.example.m13actividad2.R;
import com.google.firebase.auth.FirebaseAuth;

public class Ventana_Inicial extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText ettUsuario, ettPassword;
    private Button btEntrar;
    private ImageButton soporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this); //elimina la barra de informacion del movil (donde sale la bateria señal y eso
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {         //evita que los objetos del sistema cubran la interfaz ej: que el teclado cubra algun boton
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());             //que este en la parte de abajod e la pantalla, con esto se hace accesible el botton
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();


        // Asociar variables
        ettUsuario = findViewById(R.id.ettUsuario);
        ettPassword = findViewById(R.id.ettPassword);
        btEntrar = findViewById(R.id.btEntrar);
        soporte = findViewById(R.id.imgb_bt_soporte);

        soporte.setOnClickListener(view -> {
            Intent intent = new Intent(Ventana_Inicial.this, Ventana_auth_soporte.class);
            startActivity(intent);
            finish();
        });

        btEntrar.setOnClickListener(view -> {
            String usuario = ettUsuario.getText().toString().trim();
            String password = ettPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Utilidad.patronEmail(usuario)) {
                Toast.makeText(getApplicationContext(), "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
                return;
            }


            loginUser(usuario,password);


        });

    }

    // metodo para usar el firebase auth para validar el usuario luego llama al metodo checkUserRoleparaSeccion, que se encarga de verificar los claims del usuario y en base a ellos redirigirlo a su respectiva actividad
    public void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {  // Si el inicio de sesion es exitoso...

                        Utilidad.checkUserRoleparaSeccion(this);

                    } else {
                        // Fallo en la autenticación
                        Toast.makeText(this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}