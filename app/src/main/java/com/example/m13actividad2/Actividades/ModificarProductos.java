package com.example.m13actividad2.Actividades;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.Utilidad;

public class ModificarProductos extends AppCompatActivity {
    private EditText codigo, nombre, precio, cantidad, descripcion;
    private Producto productoRecibido;
    private Spinner spinnerCategoria;
    private ImageButton saveCodigo, saveNombre, savePrecio, saveCategoria, saveCantidad, saveDescripcion;
    private String cod, name, descriptions, categoria;
    private int stock;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_productos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        //recibimos la informacion del producto seleccionado
        productoRecibido = (Producto) getIntent().getSerializableExtra("productoSeleccionado");
        // spinner
        spinnerCategoria = findViewById(R.id.spinner_ModProducto_categoria);
        // editText
        codigo = findViewById(R.id.et_ModProducto_Codigo);
        nombre = findViewById(R.id.et_ModProducto_Nombre);
        precio = findViewById(R.id.et_ModProducto_precio);
        cantidad = findViewById(R.id.et_ModProducto_Stock);
        descripcion = findViewById(R.id.et_ModProducto_descripcion);
        // Botones
        saveCodigo = findViewById(R.id.imgbt_ModPRoducto_codigo);
        saveNombre = findViewById(R.id.imgbt_ModProducto_Nombre);
        savePrecio = findViewById(R.id.imgbt_ModProducto_precio);
        saveCategoria = findViewById(R.id.imgbt_ModProducto_categoria);
        saveCantidad = findViewById(R.id.imgbt_ModProducto_Stock);
        saveDescripcion = findViewById(R.id.imgbt_ModProducto_descripcion);

        String[] categorias = {"Selecciona una categoria","Bebidas frias", "Bebidas calientes", "Bolleria", "Pasteleria", "Comidas", "Desayunos", "Meriendas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_spinner_personalizado, categorias){
            @Override
            public boolean isEnabled(int position) {
                // Deshabilita la primera opción
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = view.findViewById(R.id.texto_spinner);
                if (position == 0) {
                    // Color gris para la opción deshabilitada
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        spinnerCategoria.setAdapter(adapter);

        codigo.setHint(productoRecibido.getCodigo());
        nombre.setHint(productoRecibido.getNombre());
        precio.setHint(String.valueOf(productoRecibido.getPrecio()));
        cantidad.setHint(String.valueOf(productoRecibido.getCantidad()));
        descripcion.setHint(productoRecibido.getDescripcion());

        saveCodigo.setOnClickListener(view -> {
            cod= codigo.getText().toString();
            if (cod.isEmpty()) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilidad.modificarCodigo(this,productoRecibido,cod,productoRecibido.getCodigo());
        });

        saveNombre.setOnClickListener(view -> {
            name = nombre.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            Utilidad.modificarProducto(this,productoRecibido.getCategoria(),"nombre",name,productoRecibido.getCodigo());
        });

        savePrecio.setOnClickListener(view -> {
            String entrada = cantidad.getText().toString().trim();

            if (entrada.isEmpty()) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
            price = Double.parseDouble(entrada);
            Utilidad.modificarProducto(this,productoRecibido.getCategoria(),"precio",price,productoRecibido.getCodigo());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Debe introducir un valor numérico válido", Toast.LENGTH_SHORT).show();
            }
        });

        saveCategoria.setOnClickListener(view -> {
            categoria = spinnerCategoria.getSelectedItem().toString().trim();
            if (categoria.equals("Selecciona una categoria")) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            Utilidad.modificarCategoria(this,productoRecibido,categoria,productoRecibido.getCategoria());
        });

        saveCantidad.setOnClickListener(view -> {
            String entrada1 = cantidad.getText().toString().trim();

            if (entrada1.isEmpty()) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int stock = Integer.parseInt(entrada1);
                Utilidad.modificarProducto(this, productoRecibido.getCategoria(), "cantidad", stock, productoRecibido.getCodigo());
                Toast.makeText(this, "Cantidad actualizada correctamente", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Debe introducir un valor numérico válido", Toast.LENGTH_SHORT).show();
            }
        });

        saveDescripcion.setOnClickListener(view -> {
            descriptions = descripcion.getText().toString().trim();
            if (descriptions.isEmpty()) {
                Toast.makeText(this, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            Utilidad.modificarProducto(this,productoRecibido.getCategoria(),"descripcion", descriptions,productoRecibido.getCodigo());

        });


    }
}