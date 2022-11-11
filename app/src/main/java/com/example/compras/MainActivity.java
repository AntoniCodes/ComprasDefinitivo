package com.example.compras;

import android.content.Intent;
import android.os.Bundle;

import com.example.compras.adapter.productosAdapter;
import com.example.compras.databinding.ActivityMainBinding;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> agregarLauncher;

    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Productos> listaProductos;

    private productosAdapter adapter;

    private NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        inicializaLunchers();
        numberFormat = numberFormat.getCurrencyInstance();


        listaProductos = new ArrayList<>();

        calculaImportes();

        adapter = new productosAdapter(MainActivity.this, listaProductos, R.layout.producto_view_holder);

        layoutManager = new LinearLayoutManager(MainActivity.this);

        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);





        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarLauncher.launch(new Intent(MainActivity.this, Agregar.class));


            }
        });
    }

    private void inicializaLunchers() {
        agregarLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {

                            if (result.getData() != null) {
                                if (result.getData().getExtras() != null) {
                                    if (result.getData().getExtras().getSerializable("PRODUCTOS") != null) {
                                        Productos productos = (Productos) result.getData().getExtras().getSerializable("PRODUCTOS");
                                        listaProductos.add(0,productos);
                                        adapter.notifyItemInserted(0);
                                        calculaImportes();



                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "El bundle no lleva el tag INMUEBLE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "NO HAY BUNDLE EN EL INTENT", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "NO HAY INTENT EN EL RESULT", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Ventana Cancelada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );





    }

    public void calculaImportes(){
        int cantidad = 0;
        float  precio = 0;
        for (Productos p :listaProductos) {
            cantidad += p.getCantidad();
            precio += p.getPrecio() * p.getCantidad();

        }
        binding.contentMain.txtCantidad.setText(String.valueOf(cantidad));
        binding.contentMain.txtImporte.setText(numberFormat.format(precio));
    }
}