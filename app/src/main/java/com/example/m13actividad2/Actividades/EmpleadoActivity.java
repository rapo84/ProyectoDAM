package com.example.m13actividad2.Actividades;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.utils.Utilidad;
import com.example.m13actividad2.R;

public class EmpleadoActivity extends AppCompatActivity {
    private Button Salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        EdgeToEdge.enable(this); //elimina la barra de informacion del movil (donde sale la bateria señal y eso
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {         //evita que los objetos del sistema cubran la interfaz ej: que el teclado cubra algun boton
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());             //que este en la parte de abajod e la pantalla, con esto se hace accesible el botton
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Salir = findViewById(R.id.bt_empleado_logout);

        Salir.setOnClickListener(view -> {
            Utilidad.cerrarSesionYRedirigir(this, Ventana_Inicial.class);
        });

    }
}