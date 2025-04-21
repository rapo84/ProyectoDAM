package com.example.m13actividad2.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.m13actividad2.Adaptadores.fragmentMesasRecyclerViewAdapter;
import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.EspacioItems;
import com.example.m13actividad2.utils.UtilidadMesas;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CATEGORIA = "categoria";

    private String categoriaSeleccionada;
    private int mColumnCount = 3;

    private fragmentMesasRecyclerViewAdapter fragmentRecyclerViewAdapter;
    private List<Producto> productos;
    private OnProductoSeleccionadoListener listener;
    private RecyclerView recyclerView;

    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    public static ItemFragment newInstance(int columnCount, String categoria) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_CATEGORIA, categoria);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            categoriaSeleccionada = getArguments().getString(ARG_CATEGORIA);
            // Verificar si la categoría se pasó correctamente
            Log.i("️CategoriaEnFragmento", "'EN EL ONCREATE' Categoría recibida en el fragmento: " + categoriaSeleccionada); // Aquí se muestra la categoría que llega al fragmento
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        Context context = view.getContext();
        Log.i("CategoriaEnFragmento", "EN EL  'ONCREATEVIEW`  ️Categoría recibida en el fragmento: " + categoriaSeleccionada); // Aquí se muestra la categoría que llega al fragmento
        // Inicializar lista y adaptador
        productos = new ArrayList<>();

        /*fragmentRecyclerViewAdapter = new fragmentMesasRecyclerViewAdapter(productos);*/

        fragmentRecyclerViewAdapter = new fragmentMesasRecyclerViewAdapter(productos, producto -> {
            if (getActivity() instanceof OnProductoSeleccionadoListener) {
                ((OnProductoSeleccionadoListener) getActivity()).onProductoSeleccionado(producto);
            }
        });

        // Configurar RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(fragmentRecyclerViewAdapter);
        int espacioItem = (int) (8 * context.getResources().getDisplayMetrics().density);
        recyclerView.addItemDecoration(new EspacioItems(espacioItem));

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // Cargar productos desde Firebase
        UtilidadMesas.cargarProductosPorCategoria(context, productos, fragmentRecyclerViewAdapter, categoriaSeleccionada);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProductoSeleccionadoListener) {
            listener = (OnProductoSeleccionadoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar OnProductoSeleccionadoListener");
        }
    }

    public interface OnProductoSeleccionadoListener {
        void onProductoSeleccionado(Producto producto);
    }
}
