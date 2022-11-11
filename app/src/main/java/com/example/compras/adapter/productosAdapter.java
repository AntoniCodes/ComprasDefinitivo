package com.example.compras.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.compras.MainActivity;
import com.example.compras.Productos;
import com.example.compras.R;
import com.example.compras.databinding.ActivityMainBinding;

import java.text.NumberFormat;
import java.util.ArrayList;

public class productosAdapter extends RecyclerView.Adapter<productosAdapter.TodoVH> {

    private Context context;

    private ArrayList<Productos> objetos;

    private ActivityMainBinding binding;

    private int cardLayout;

    private NumberFormat NumberFormat;

    private MainActivity main;

    public productosAdapter(Context context, ArrayList<Productos> objetos, int cardLayout) {
        this.context = context;
        this.objetos = objetos;
        this.cardLayout = cardLayout;
        this.main = (MainActivity) context;
        NumberFormat = NumberFormat.getCurrencyInstance();
    }

    @NonNull
    @Override
    public TodoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View toDoView = LayoutInflater.from(context).inflate(cardLayout, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toDoView.setLayoutParams(layoutParams);
        return new TodoVH(toDoView);
    }



    @Override
    public void onBindViewHolder(@NonNull TodoVH holder, int position) {
        Productos productos = objetos.get(position);
        holder.lblNombreProducto.setText(productos.getNombre());
        holder.lblCantidadProducto.setText(String.valueOf(productos.getCantidad()));
        holder.lblPrecioProducto.setText(NumberFormat.format(productos.getPrecio()));

        holder.btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmaEliminacion("Estas seguro de eliminar "+productos.getNombre().toString(),productos).show();
                notifyDataSetChanged();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog con todos los campos a Editar
                // Necesita el producto
                // Necesita la Posición
                editProducto(productos, holder.getAdapterPosition()).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return objetos.size();
    }



    public class TodoVH extends RecyclerView.ViewHolder{

        TextView lblNombreProducto, lblCantidadProducto, lblPrecioProducto;
        ImageButton btnEliminarProducto;
        public TodoVH(@NonNull View itemView) {
            super(itemView);
            lblNombreProducto = itemView.findViewById(R.id.lblNombreProducto);
            lblCantidadProducto = itemView.findViewById(R.id.lblCantidadProducto);
            lblPrecioProducto = itemView.findViewById(R.id.lblPrecioProducto);
            btnEliminarProducto = itemView.findViewById(R.id.btnEliminarProducto);

        }
    }

    public AlertDialog confirmaEliminacion(String titulo, Productos producto){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setCancelable(false);
        builder.setNegativeButton("no",null);
        builder.setPositiveButton("si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                objetos.remove(producto);
                notifyDataSetChanged();
                main.calculaImportes();
            }
        });
        return builder.create();
    }

    private AlertDialog editProducto(Productos producto, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Editar Producto");
        builder.setCancelable(false);

        // Necesitamos un conenido
        View productoView = LayoutInflater.from(context).inflate(R.layout.producto_view_alert, null);
        EditText txtNombre = productoView.findViewById(R.id.txtNombreProductoAlert);
        EditText txtCantidad = productoView.findViewById(R.id.txtCantidadProductoAlert);
        EditText txtPrecio = productoView.findViewById(R.id.txtPrecioProductoAlert);

        builder.setView(productoView);

        txtNombre.setText(producto.getNombre());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecio()));


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(android.text.Editable s) {

                    String nombre = txtNombre.getText().toString();
                    String cantidad = txtCantidad.getText().toString();
                    String precio = txtPrecio.getText().toString();



                }


        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    producto.setNombre(txtNombre.getText().toString());
                    producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                    producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                    main.calculaImportes();


                    notifyItemChanged(adapterPosition);
                } else {
                    Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancelar", null);


        return builder.create();
    }



}
