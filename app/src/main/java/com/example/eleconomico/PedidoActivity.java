package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PedidoActivity extends AppCompatActivity {

    private Button btnRealizarPedido;
    private RecyclerView recyclerViewProductos;
    private ProductoAdapter productoAdapter;
    private List<Producto> productosSeleccionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

        // Demo, reemplazar con llamada API real para traer productos
        List<Producto> productos = obtenerProductosDemo();

        productoAdapter = new ProductoAdapter(productos, producto -> {
            productosSeleccionados.add(producto);
            Toast.makeText(this, producto.getNombre() + " agregado al pedido", Toast.LENGTH_SHORT).show();
        });

        recyclerViewProductos.setAdapter(productoAdapter);

        btnRealizarPedido.setOnClickListener(v -> {
            if (productosSeleccionados.isEmpty()) {
                Toast.makeText(this, "Seleccione al menos un producto", Toast.LENGTH_SHORT).show();
                return;
            }
            // Aquí puedes llamar a API para enviar el pedido con los productos seleccionados
            Toast.makeText(this, "Pedido realizado con " + productosSeleccionados.size() + " productos", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private List<Producto> obtenerProductosDemo() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto("1", "Six Pack Corona (2 unidades)", 800.00));
        lista.add(new Producto("2", "Pañales para bebé 45 unidades", 750.00));
        lista.add(new Producto("3", "Leche Ceteco 500g", 450.00));
        lista.add(new Producto("4", "10 Libras de Chuleta", 380.00));
        lista.add(new Producto("5", "Cerveza Paquete de 6", 15.50));
        return lista;
    }
}
