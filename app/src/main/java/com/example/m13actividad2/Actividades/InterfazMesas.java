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
    private String mesaSeleccionada = "Mesa06";     //⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️ inicializarla sin valor ya que el valor lo recuperamos con la actividad anterior


    @Override
    public void onProductoSeleccionado(Producto producto) {
        productosTemporales.add(producto);
        //adaptador_listas.notifyItemInserted(productosTemporales.size() - 1);
        adaptador_listas.actualizarLista(productosTemporales);
        // ✅ Verificar tamaño de la lista
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

        //obtenemos el numero de mesa de la actividad anterior que nos la pasa con el intent   ⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️
        //mesaSeleccionada = "Mesa"+getIntent().getStringExtra("numeroMesa");


        adapter = new CategoriasAdapter(categ, new CategoriasAdapter.OnCategoriaClickListener() {
            @Override
            public void onCategoriaClick(String categoria) {

                Log.d("CategoriaSeleccionada", "Categoría seleccionada: " + categoria);  // Aquí se muestra el valor de la categoría
                // Aquí se llama al fragment y se le pasa la categoría
                ItemFragment fragment = ItemFragment.newInstance(3, categoria);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedorFragmentProductos, fragment)
                        .commit();
            }
        });


        rv_categorias.setAdapter(adapter);

        adaptador_listas = new Lista_orden_adaptador(productosTemporales);
        rv_temporalOrders.setAdapter(adaptador_listas);

        /*
        // Configura el listener para el click en los items del recyclerview de la lista temporal
        adaptador_listas.setOnItemClickListener(new Lista_orden_adaptador.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto, int position) {
                // Aquí manejas el click en el item
                adaptador_listas.mostrarDialogoConfirmacion(InterfazMesas.this,producto, position, productosTemporales);
                /*borramos por completo la lista temporal para reemplazarla por la lista actualizada de esta manera evitamos que se repitan los productos y
                 solo se van modificando las cantidades y dentro de la lista quedan los objetos productos con sus respectivas cantidades  ⚠️⚠️⚠️*/ /*
                productosTemporales.clear();
                productosTemporales = adaptador_listas.getListarProductos();
                adaptador_listas.notifyDataSetChanged();
            }
        });*/

        adaptador_listas.setOnItemClickListener(new Lista_orden_adaptador.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto, int position) {
                // Aquí manejas el click en el item
                adaptador_listas.mostrarDialogoConfirmacion(InterfazMesas.this,producto, position, productosTemporales);
            }
        });

        UtilidadMesas.obtenercategorias(this,categ, adapter);

        check.setOnClickListener(v -> {

            lispapasable=adaptador_listas.getListarProductos();
            Log.e("TamañoLista", "🟢 Total productos temporales: " + lispapasable.size());
            for (Producto p : lispapasable) {
                Log.i("ListaActual", "🔹 Producto: " + p.getNombre());
            }
            UtilidadMesas.guardarPedidosEnElHistorial(this,lispapasable,mesaSeleccionada);  //⚠️⚠️⚠️⚠️⚠️⚠️ cambiar la variable de mesa por la que corresponda
            productosTemporales.clear();
            adaptador_listas.setListaProductos(new ArrayList<>());  // Esto debe actualizar la lista interna
            adaptador_listas.notifyDataSetChanged(); // Y forzar el refresco visual

        });

        all_orders.setOnClickListener(v -> {
            Intent intent = new Intent(InterfazMesas.this, Listado_ordenes_varias.class);
            intent.putExtra("numeroMesa", mesaSeleccionada);  //⚠️⚠️⚠️⚠️⚠️⚠️ cambiar la variable de mesa por la que corresponda
            startActivity(intent);

        });







    }
}