package com.example.m13actividad2.Actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.m13actividad2.Modelos.Producto;
import com.example.m13actividad2.R;
import com.example.m13actividad2.utils.Utilidad;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarProductos extends AppCompatActivity {

    EditText codigo, nombre, precio, categoria, cantidad;
    Button agregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_productos);

        codigo = findViewById(R.id.eTCod);
        nombre = findViewById(R.id.eTNom);
        precio = findViewById(R.id.eTPrecio);
        categoria = findViewById(R.id.eTCat);
        cantidad = findViewById(R.id.eTCantidad);
        agregar = findViewById(R.id.btnAgregar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cod = codigo.getText().toString();
                String nom = nombre.getText().toString();
                String cat = categoria.getText().toString();
                double pre = 0.0;
                int can = 0;
                try {
                    pre = Double.parseDouble(precio.getText().toString());
                    can = Integer.parseInt(cantidad.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(AgregarProductos.this, "Error al convertir el precio o la cantidad", Toast.LENGTH_SHORT).show();
                    return;
                }

                Producto producto = new Producto(cod, nom, pre, cat, can);

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("Locales").child(Utilidad.recupernombrelocal(AgregarProductos.this)).child("Productos");

                ref.child(cat).child(cod).setValue(producto).addOnCompleteListener(Task ->{
                    if (Task.isSuccessful()){
                        Toast.makeText(AgregarProductos.this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AgregarProductos.this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}