package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.utils.Utilidad;
import com.example.m13actividad2.R;
import com.google.firebase.auth.FirebaseAuth;

public class Ventana_Inicial extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText ettUsuario, ettPassword;
    private Button btEntrar;
    private ImageButton soporte;

    private static final int REQUEST_LOCATION_PERMISSION = 1; // Código para solicitar permisos
    private ActivityResultLauncher<Intent> enableBluetoothLauncher; // Launcher para habilitar el Bluetooth

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

        // revisamos que el bluetooth este activado y en caso de no estarlo pedimos su activacion
        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("Bluetooth", "Bluetooth habilitado por el usuario");
                    } else {
                        Toast.makeText(this, "Para poder usar la app es obligatorio el uso del Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                });

        // verificamos que los permisos esten concedidos y en caso de que no lo esten se piden al usuario
        if (!checkPermissions()) {
            requestPermissions(); // Si no se tienen permisos, entonces los pedimos
        }

        soporte.setOnClickListener(view -> {
            Intent intent = new Intent(Ventana_Inicial.this, Ventana_auth_soporte.class);
            startActivity(intent);
            finish();
        });

        btEntrar.setOnClickListener(view -> {
            String usuario = ettUsuario.getText().toString().trim();
            String password = ettPassword.getText().toString().trim();

            // Verificar que los campos no estén vacíos
            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }
            // Validar el formato del correo electrónico
            if (!Utilidad.patronEmail(usuario)) {
                Toast.makeText(getApplicationContext(), "Formato de correo electrónico no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamamos al método de autenticación con Firebase
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
    // Método para verificar si los permisos necesarios están concedidos
    private boolean checkPermissions() {
        // Para versiones de Android más nuevas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Para versiones anteriores de Android
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
        }
    }
    // Método para solicitar permisos al usuario
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Solicitar permisos para Bluetooth en Android 12 y versiones posteriores
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN
                    },
                    REQUEST_LOCATION_PERMISSION);
        } else {
            // Solicitar permisos para Bluetooth en versiones anteriores de Android
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN
                    },
                    REQUEST_LOCATION_PERMISSION);
        }
    }
    // Método para manejar los resultados de la solicitud de permisos y actuar segun la situacion que exista
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false; // Si algún permiso no fue concedido
                    break;
                }
            }

            if (!allGranted) {
                Toast.makeText(this, "Es necesario aceptar los permisos para continuar", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Permisos", "Todos los permisos concedidos");
            }
        }
    }




}