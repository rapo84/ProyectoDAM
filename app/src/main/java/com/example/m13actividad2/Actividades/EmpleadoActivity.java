package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.Utilidad;

public class EmpleadoActivity extends AppCompatActivity {
    GridLayout gridLayoutMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridLayoutMesas = findViewById(R.id.gridLayoutMesas);

        int numMesas = Utilidad.recupernumMesas(this);
        crearBotonesDeMesa(numMesas);
    }

    private void crearBotonesDeMesa(int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            LinearLayout layoutMesa = new LinearLayout(this);
            layoutMesa.setOrientation(LinearLayout.VERTICAL);
            layoutMesa.setGravity(Gravity.CENTER);
            layoutMesa.setPadding(8, 8, 8, 8);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            layoutMesa.setLayoutParams(params);

            ImageButton btnMesa = new ImageButton(this);
            btnMesa.setImageResource(R.drawable.mesapic);
            btnMesa.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            btnMesa.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            );
            btnMesa.setLayoutParams(btnParams);

            final int numeroMesa = i;
            btnMesa.setOnClickListener(v -> {
                Intent intent = new Intent(EmpleadoActivity.this, InterfazMesas.class);
                intent.putExtra("numeroMesa", numeroMesa);
                startActivity(intent);
            });

            TextView textMesa = new TextView(this);
            textMesa.setText("Mesa: " + i);
            textMesa.setGravity(Gravity.CENTER);

            layoutMesa.addView(btnMesa);
            layoutMesa.addView(textMesa);
            gridLayoutMesas.addView(layoutMesa);
        }
    }
}
