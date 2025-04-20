package com.example.m13actividad2.Actividades;

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
import com.example.m13actividad2.Fragment.ItemFragment;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.UtilidadMesas;

import java.util.ArrayList;
import java.util.List;

public class InterfazMesas extends AppCompatActivity {
    private FrameLayout fragment1;
    private ImageButton check, borrar, cambiar, opc;
    private Button all_orders;
    private RecyclerView rv_categorias;
    private List<String> categ;
    private CategoriasAdapter adapter;


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

        categ = new ArrayList<>();
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


        UtilidadMesas.obtenercategorias(this,categ, adapter);







    }
}