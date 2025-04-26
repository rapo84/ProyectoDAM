package com.example.m13actividad2.Actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.ListaFinalAdaptador;
import com.example.m13actividad2.ConextionsPrint.ImpresionBt;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.interfaces.ProductosCargadosListener;
import com.example.m13actividad2.interfaces.RoleCheckCallback;
import com.example.m13actividad2.utils.Utilidad;
import com.example.m13actividad2.utils.UtilidadMesas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Listado_ordenes_varias extends AppCompatActivity {
    private TextView cantidad, nombre, precio, tv_total;
    private Button Imprimir, ImprimirYcerrarMesa;
    private RecyclerView rv_listas_ordenes_historial;
    private ListaFinalAdaptador adaptador;
    private List<Producto> lista;
    private int numMesa =5;
    private String mesaSeleccionada;
    Double totalFormateado;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_ordenes_varias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cantidad = findViewById(R.id.tv_Lista_final_cantidad);
        nombre = findViewById(R.id.tv_Lista_final_nombre);
        precio = findViewById(R.id.tv_Lista_final_Precio);
        Imprimir = findViewById(R.id.bt_lista_final_imprimir);
        ImprimirYcerrarMesa = findViewById(R.id.bt_lista_final_imprimirYcerrarMesa);
        tv_total = findViewById(R.id.tv_lista_final_total);

        mesaSeleccionada= getIntent().getStringExtra("numeroMesa");

        rv_listas_ordenes_historial = findViewById(R.id.rv_listas_ordenes_historial);
        rv_listas_ordenes_historial.setLayoutManager(new LinearLayoutManager(this));

        lista = new ArrayList<>();

        UtilidadMesas.cargarHistorialdeProductosDeMesas(this, mesaSeleccionada, new ProductosCargadosListener() {
            @Override
            public void callbackProductosCargados(List<Producto> productos) {
                // en este punto ya tenemos los productos esperando a ver que hacemos con ellos
                Log.d("Productos", "Se cargaron " + productos.size() + " productos.");
                total = 0;
                lista.addAll(productos);
                for (Producto p : productos) {
                    Double subtotal= (p.getPrecio()*p.getCantidad());
                    total += subtotal;

                }
                totalFormateado = Math.round(total * 100.0) / 100.0;  // redondear a dos decimales
                // Creamos un adaptador con los datos cargados y lo asignamos al RecyclerView
                adaptador=new ListaFinalAdaptador(productos, Listado_ordenes_varias.this, mesaSeleccionada);
                rv_listas_ordenes_historial.setAdapter(adaptador);
                tv_total.setText(String.valueOf(totalFormateado));

                Imprimir.setEnabled(true); // activar cuando ya está todo cargado
                ImprimirYcerrarMesa.setEnabled(true); // activar cuando ya está todo cargado

            }
        });

        Imprimir.setOnClickListener(view -> {

            ImpresionBt.Imprimir(this, lista, totalFormateado, mesaSeleccionada);

        });

        ImprimirYcerrarMesa.setOnClickListener(view -> {
            //ImpresionBt.Imprimir(this, lista, totalFormateado, mesaSeleccionada);
            UtilidadMesas.mostrarDialogoConfirmacionLiberarmesa("Estas seguro que quieres liberar la mesa, esto ELIMINARA por completo los datos de la mesa?", this, mesaSeleccionada, lista, totalFormateado);

        });

    }


}