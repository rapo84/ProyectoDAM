package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.m13actividad2.utils.Utilidad;
import com.example.m13actividad2.interfaces.ApiService;
import com.example.m13actividad2.network.ClaimsResponse;
import com.example.m13actividad2.network.RetrofitClient;
import com.example.m13actividad2.network.TokenRequest;
import com.example.m13actividad2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ventana_auth_soporte extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtUsuario, edtPassword;
    private Button btEntrar, volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_auth_soporte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();


        // Asociar variables
        edtUsuario = findViewById(R.id.edt_Usuario_soporte);
        edtPassword = findViewById(R.id.edt_Password_soporte);
        btEntrar = findViewById(R.id.bt_Entrar_soporte);
        volver = findViewById(R.id.bt_volver_soporte);

        volver.setOnClickListener(view -> {
            Intent intent = new Intent(Ventana_auth_soporte.this, Ventana_Inicial.class);
            startActivity(intent);
            finish();
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = edtUsuario.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (usuario.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Utilidad.patronEmail(usuario)) {
                    Toast.makeText(getApplicationContext(), "El email no tiene el formato correcto", Toast.LENGTH_SHORT).show();
                    return;
                }
                    loginUser(usuario, password);

            }
        });
    }



    /*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                            ESTOS METODOS A CONTINUACION PARECEN REPETIRSE PERO NO ES ASI,
                            ESTOS ESTAN AJUSTADOS SOLO PARA VERIFICAR A UN SUPERADMIN Y NO SE REPITEN EN NINGUNA OTRA ACTIVIDAD
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/

    // metodo para usar el firebase auth para validar el usuario (solo valida no dice si es admin o empleado para ello es otro metodo) que si lo usamos dentro de este pero la logica es aparte
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        checkSuperUserRole("superUser");  // ojo mayusculas
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    } else {
                        // Fallo en la autenticación
                        Toast.makeText(this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void checkSuperUserRole(String rol) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getIdToken(true) // 'true' para forzar la renovación del token
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            // Envía el idToken al servidor para verificarlo
                            ApiService apiService= RetrofitClient.getApiService();
                            TokenRequest tokenRequest = new TokenRequest(idToken);

                            apiService.getSuperClaims(tokenRequest).enqueue(new Callback<ClaimsResponse>() {  // esta linea varia segun superUser o persona normal
                                @Override
                                public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        ClaimsResponse claimsResponse = response.body();
                                        String role = claimsResponse.getRole(); // El rol del usuario
                                        if (rol.equals(role)) {
                                            Intent intent = new Intent(Ventana_auth_soporte.this, VentanaSoporte.class); //modificar aqui la actividad a redirigir
                                            startActivity(intent);
                                            finish();
                                            Log.d("✅ Firebase", "El usuario es admin");
                                        } else {
                                            Log.d("❌ Firebase", "El usuario NO es admin");
                                        }
                                    } else {
                                        Log.e("❌ Error", "Error al obtener claims");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                                    Log.e("❌ Error", "Error al hacer la petición: " + t.getMessage());
                                }
                            });
                        } else {
                            Log.e("❌ Firebase", "Error al obtener el token", task.getException());
                        }
                    });
        }
    }


}