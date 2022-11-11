package com.example.compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.compras.databinding.ActivityAgregarBinding;

public class Agregar extends AppCompatActivity {
    private ActivityAgregarBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.lblCrearNombre.getText().toString().isEmpty() || binding.lblCrearCantidad.getText().toString().isEmpty() || binding.lblPrecio.getText().toString().isEmpty()){
                    Toast.makeText(Agregar.this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                }else{
                    Productos productos = new Productos(binding.lblCrearNombre.getText().toString(),
                            Float.parseFloat(binding.lblPrecio.getText().toString()),
                            Integer.parseInt(binding.lblCrearCantidad.getText().toString()));


                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PRODUCTOS", productos);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

    });
    }
}