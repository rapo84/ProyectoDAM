package com.example.m13actividad2.Actividades;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.ListaFinalAdaptador;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.interfaces.ProductosCargadosListener;
import com.example.m13actividad2.utils.UtilidadMesas;

import java.util.List;

public class Listado_ordenes_varias extends AppCompatActivity {
    private TextView cantidad, nombre, precio, tv_total;
    private ImageButton bt_borrar;
    private RecyclerView rv_listas_ordenes_historial;
    private ListaFinalAdaptador adaptador;
    private List<Producto> lista;
    private int numMesa =5;
    private String mesaSeleccionada, totalFormateado;
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
        bt_borrar = findViewById(R.id.imgb_Lista_final_borrar);
        tv_total = findViewById(R.id.tv_lista_final_total);

        mesaSeleccionada= getIntent().getStringExtra("numeroMesa");

        rv_listas_ordenes_historial = findViewById(R.id.rv_listas_ordenes_historial);
        rv_listas_ordenes_historial.setLayoutManager(new LinearLayoutManager(this));

        UtilidadMesas.cargarHistorialdeProductosDeMesas(this, mesaSeleccionada, new ProductosCargadosListener() {
            @Override
            public void callbackProductosCargados(List<Producto> productos) {
                // en este punto ya tenemos los productos esperando a ver que hacemos con ellos
                Log.d("Productos", "Se cargaron " + productos.size() + " productos.");
                total = 0;
                for (Producto p : productos) {
                    Double subtotal= (p.getPrecio()*p.getCantidad());
                    total += subtotal;
                    totalFormateado = String.format("%.2f", total);
                }

                // Creamos un adaptador con los datos cargados y lo asignamos al RecyclerView
                adaptador=new ListaFinalAdaptador(productos, Listado_ordenes_varias.this, mesaSeleccionada);
                rv_listas_ordenes_historial.setAdapter(adaptador);
                tv_total.setText(String.valueOf(totalFormateado));

            }
        });





    }
}