package com.example.m13actividad2.Actividades;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.Utilidad;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarProductos extends AppCompatActivity {

    EditText codigo, nombre, precio, cantidad, descripcion;
    Button agregar;
    Spinner categoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_productos);

        codigo = findViewById(R.id.eTCod);
        nombre = findViewById(R.id.eTNom);
        precio = findViewById(R.id.eTPrecio);
        cantidad = findViewById(R.id.eTCantidad);
        agregar = findViewById(R.id.btnAgregar);
        categoria = findViewById(R.id.SpinCat);
        descripcion = findViewById(R.id.etDesc);

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
        categoria.setAdapter(adapter);

        agregar.setOnClickListener(v -> {
            if (codigo.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || precio.getText().toString().isEmpty()
                    || cantidad.getText().toString().isEmpty() || categoria.getSelectedItem().toString().isEmpty() || descripcion.getText().toString().isEmpty()) {
                Toast.makeText(AgregarProductos.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            String cod = codigo.getText().toString();
            String nom = nombre.getText().toString();
            String cat = categoria.getSelectedItem().toString();
            String desc = descripcion.getText().toString();

            double pre;
            int can;
            try {
                pre = Double.parseDouble(precio.getText().toString());
                can = Integer.parseInt(cantidad.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(AgregarProductos.this, "Error al convertir el precio o la cantidad", Toast.LENGTH_SHORT).show();
                return;
            }

            Producto producto = new Producto(cod, nom, pre,desc, cat, can);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("Locales").child(Utilidad.recupernombrelocal(AgregarProductos.this)).child("Productos");

            ref.child(cat).child(cod).setValue(producto).addOnCompleteListener(Task ->{
                if (Task.isSuccessful()){
                    Toast.makeText(AgregarProductos.this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AgregarProductos.this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}