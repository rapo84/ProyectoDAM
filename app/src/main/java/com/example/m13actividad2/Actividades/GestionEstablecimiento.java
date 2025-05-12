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
import com.example.m13actividad2.utils.UtilidadMesas;

public class GestionEstablecimiento extends AppCompatActivity {
    private ImageButton GuardarNumMesas, EnlazarPrint, GuardarCorreo, GuardarTelefono, GuardarIva;
    private EditText EtnumMesas, EtCorreo, EtTelefono, etPorcentajeIva;
    private int NumeroDeMesas;
    private Button Salir;


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
        GuardarIva = findViewById(R.id.bt_opc_establecimiento_guardar_porcentaje);
        EtCorreo = findViewById(R.id.et_opc_establecimiento_mail);
        EtTelefono = findViewById(R.id.et_opc_establecimiento_telefono);
        etPorcentajeIva = findViewById(R.id.et_porcentaje_iva);
        Salir = findViewById(R.id.bt_GE_logout);




        GuardarNumMesas.setOnClickListener(view -> {
            NumeroDeMesas = 0;
            String entrada = EtnumMesas.getText().toString().trim();

            if (entrada.isEmpty()) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                NumeroDeMesas = Integer.parseInt(entrada);
                if (NumeroDeMesas>0){
                    UtilidadMesas.GuardarDatosPieDeTicket(this,"NumeroDeMesas",String.valueOf(NumeroDeMesas));
                }else {
                    Toast.makeText(this, "El numero de mesas no puede ser menos a 0", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                // Manejar el error: puedes mostrar un mensaje o asignar un valor predeterminado
                Toast.makeText(this, "Ingrese un número válido", Toast.LENGTH_SHORT).show();
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
                UtilidadMesas.GuardarDatosPieDeTicket(this,"Correo",Correo);
            }
        });

        GuardarTelefono.setOnClickListener(view -> {
            String Telefono = EtTelefono.getText().toString().trim();
            if (Telefono.isEmpty()){
                Toast.makeText(this, "Ingrese un telefono", Toast.LENGTH_SHORT).show();
            }else {
                UtilidadMesas.GuardarDatosPieDeTicket(this,"Telefono",Telefono);

            }
        });

        GuardarIva.setOnClickListener(view -> {
            if (etPorcentajeIva.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Ingrese un porcentaje de iva", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double Iva = Double.parseDouble(etPorcentajeIva.getText().toString().trim());
                if (Iva>0){
                    UtilidadMesas.GuardarDatosPieDeTicket(this,"Iva",Iva.toString());
                }else {
                    Toast.makeText(this, "Ingrese un numero valido", Toast.LENGTH_SHORT).show();
                }
            }catch (NumberFormatException e){
                Toast.makeText(this, "Ingrese un numero valido", Toast.LENGTH_SHORT).show();
            }

        });

        Salir.setOnClickListener(view -> {
            Utilidad.cerrarSesionYRedirigir(this, Ventana_Inicial.class);
        });


    }
}