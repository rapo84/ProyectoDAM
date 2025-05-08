package com.example.m13actividad2.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m13actividad2.Adaptadores.CategoriasAdapter;
import com.example.m13actividad2.Adaptadores.Lista_orden_adaptador;
import com.example.m13actividad2.ConextionsPrint.ImpresionBt;
import com.example.m13actividad2.Fragment.ItemFragment;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.UtilidadMesas;

import java.util.ArrayList;
import java.util.List;

public class InterfazMesas extends AppCompatActivity implements ItemFragment.OnProductoSeleccionadoListener {
    private FrameLayout fragment1;
    private ImageButton check, borrar, cambiar, opc;
    private Button all_orders;
    private RecyclerView rv_categorias, rv_temporalOrders;
    private List<String> categ;
    private CategoriasAdapter adapter;
    private Lista_orden_adaptador adaptador_listas;
    private List<Producto> productosTemporales, lispapasable;
    private String mesaSeleccionada = "Mesa06";  // ⚠️ cambiar por valor dinámico si es necesario

    @Override
    public void onProductoSeleccionado(Producto producto) {
        productosTemporales.add(producto);
        adaptador_listas.actualizarLista(productosTemporales);
        Log.d("TamañoLista", "🟢 Total productos temporales: " + productosTemporales.size());
        for (Producto p : productosTemporales) {
            Log.d("ListaActual", "🔹 Producto: " + p.getNombre());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_interfaz_mesas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragment1 = findViewById(R.id.contenedorFragmentProductos);
        check = findViewById(R.id.bt_mesas_Imprimir);
        opc = findViewById(R.id.bt_mesas_opciones);
        borrar = findViewById(R.id.bt_mesas_BorrarProducto);
        all_orders = findViewById(R.id.bt_mesas_all_Orders);

        rv_categorias = findViewById(R.id.rvCategorias);
        rv_categorias.setLayoutManager(new LinearLayoutManager(this));

        rv_temporalOrders = findViewById(R.id.rv_orders_temporales);
        rv_temporalOrders.setLayoutManager(new LinearLayoutManager(this));

        categ = new ArrayList<>();
        productosTemporales = new ArrayList<>();

        // mesaSeleccionada = "Mesa"+getIntent().getStringExtra("numeroMesa");

        adapter = new CategoriasAdapter(categ, categoria -> {
            Log.d("CategoriaSeleccionada", "Categoría seleccionada: " + categoria);
            ItemFragment fragment = ItemFragment.newInstance(3, categoria);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedorFragmentProductos, fragment)
                    .commit();
        });
        rv_categorias.setAdapter(adapter);

        adaptador_listas = new Lista_orden_adaptador(productosTemporales);
        rv_temporalOrders.setAdapter(adaptador_listas);

        adaptador_listas.setOnItemClickListener((producto, position) -> {
            adaptador_listas.mostrarDialogoConfirmacion(InterfazMesas.this, producto, position, productosTemporales);
        });

        UtilidadMesas.obtenercategorias(this, categ, adapter);

        check.setOnClickListener(v -> {
            lispapasable = adaptador_listas.getListarProductos();
            ImpresionBt.ImprimirListaTemporales(this, lispapasable, mesaSeleccionada);
            Log.e("TamañoLista", "🟢 Total productos temporales: " + lispapasable.size());
            for (Producto p : lispapasable) {
                Log.i("ListaActual", "🔹 Producto: " + p.getNombre());
            }
            UtilidadMesas.guardarPedidosEnElHistorial(this, lispapasable, mesaSeleccionada);
            productosTemporales.clear();
            adaptador_listas.setListaProductos(new ArrayList<>());
            adaptador_listas.notifyDataSetChanged();
        });

        all_orders.setOnClickListener(v -> {
            Intent intent = new Intent(InterfazMesas.this, Listado_ordenes_varias.class);
            intent.putExtra("numeroMesa", mesaSeleccionada);
            startActivity(intent);
        });
    }
}