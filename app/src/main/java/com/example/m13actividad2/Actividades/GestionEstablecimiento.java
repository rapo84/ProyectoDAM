package com.example.m13actividad2.Actividades;

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

import com.example.m13actividad2.ConextionsPrint.ImpresionBt;
import com.example.m13actividad2.R;
import com.example.m13actividad2.interfaces.SelecconImpresoraCallBack;
import com.example.m13actividad2.utils.Utilidad;

public class GestionEstablecimiento extends AppCompatActivity {
    private ImageButton GuardarNumMesas, EnlazarPrint, GuardarCorreo, GuardarTelefono;
    private EditText EtnumMesas, EtCorreo, EtTelefono;
    private int NumeroDeMesas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_establecimiento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EnlazarPrint = findViewById(R.id.imgbt_opc_establecimiento_setImpresora);
        GuardarNumMesas = findViewById(R.id.bt_opc_establecimiento_guardarnumMesas);
        EtnumMesas = findViewById(R.id.et_opc_establecimiento_numero_mesas);
        GuardarCorreo = findViewById(R.id.bt_opc_establecimiento_guardar_mail);
        GuardarTelefono = findViewById(R.id.bt_opc_establecimiento_guardar_telefono);
        EtCorreo = findViewById(R.id.et_opc_establecimiento_mail);
        EtTelefono = findViewById(R.id.et_opc_establecimiento_telefono);


        GuardarNumMesas.setOnClickListener(view -> {
            NumeroDeMesas = 0;
            try {
                NumeroDeMesas = Integer.parseInt(EtnumMesas.getText().toString().trim());
            } catch (NumberFormatException e) {
                // Manejar el error: puedes mostrar un mensaje o asignar un valor predeterminado
                Toast.makeText(this, "Ingrese un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (NumeroDeMesas>0){
                Utilidad.guardarnumMEsas(this,NumeroDeMesas);
            }else {
                Toast.makeText(this, "El numero de mesas no puede ser menos a 0", Toast.LENGTH_SHORT).show();
            }


        });

        EnlazarPrint.setOnClickListener(view -> {
            ImpresionBt.SeleccionarImpresora(this, new SelecconImpresoraCallBack() {
                @Override
                public void onImpresoraSeleccionada(String printerName) {
                    Utilidad.guardarDatoLocalmente(GestionEstablecimiento.this,printerName,"Impresora");
                }
            });

        });

        GuardarCorreo.setOnClickListener(view -> {
            String Correo = EtCorreo.getText().toString().trim();
            if (Correo.isEmpty()){
                Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
            }else {
                Utilidad.guardarDatoLocalmente(this,Correo,"Correo");
            }
        });

        GuardarTelefono.setOnClickListener(view -> {
            String Telefono = EtTelefono.getText().toString().trim();
            if (Telefono.isEmpty()){
                Toast.makeText(this, "Ingrese un telefono", Toast.LENGTH_SHORT).show();
            }else {
                Utilidad.guardarDatoLocalmente(this,Telefono,"Telefono");

            }
        });


    }
}