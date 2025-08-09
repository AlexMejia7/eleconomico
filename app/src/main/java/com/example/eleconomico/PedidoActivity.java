package com.example.eleconomico;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PedidoActivity extends AppCompatActivity {

    private Button btnRealizarPedido, btnGuardarPedido, btnVerPedidos;
    private RecyclerView recyclerViewProductos, recyclerViewSeleccionados;
    private ProductoAdapter productoAdapter;
    private ProductoAdapter seleccionadoAdapter;

    private List<Producto> productosDisponibles = new ArrayList<>();
    private List<Producto> productosSeleccionados = new ArrayList<>();

    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        btnGuardarPedido = findViewById(R.id.btnGuardarPedido);
        btnVerPedidos = findViewById(R.id.btnVerPedidos);

        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        recyclerViewSeleccionados = findViewById(R.id.recyclerViewSeleccionados);
        tvTotal = findViewById(R.id.tvTotal);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSeleccionados.setLayoutManager(new LinearLayoutManager(this));

        productosDisponibles = obtenerProductosDemo();

        productoAdapter = new ProductoAdapter(productosDisponibles, producto -> {
            productosSeleccionados.add(producto);
            seleccionadoAdapter.notifyDataSetChanged();
            actualizarTotal();
            Toast.makeText(this, producto.getNombre() + " agregado al pedido", Toast.LENGTH_SHORT).show();
        });

        seleccionadoAdapter = new ProductoAdapter(productosSeleccionados, producto -> {
            productosSeleccionados.remove(producto);
            seleccionadoAdapter.notifyDataSetChanged();
            actualizarTotal();
            Toast.makeText(this, producto.getNombre() + " removido del pedido", Toast.LENGTH_SHORT).show();
        });

        recyclerViewProductos.setAdapter(productoAdapter);
        recyclerViewSeleccionados.setAdapter(seleccionadoAdapter);

        btnRealizarPedido.setOnClickListener(v -> {
            if (productosSeleccionados.isEmpty()) {
                Toast.makeText(this, "Seleccione al menos un producto", Toast.LENGTH_SHORT).show();
                return;
            }
            // Aquí puedes mostrar resumen, preparar para enviar o algo similar
            Toast.makeText(this, "Pedido realizado con " + productosSeleccionados.size() + " productos", Toast.LENGTH_SHORT).show();
        });

        btnGuardarPedido.setOnClickListener(v -> {
            if (productosSeleccionados.isEmpty()) {
                Toast.makeText(this, "No hay productos para guardar", Toast.LENGTH_SHORT).show();
                return;
            }
            guardarPedido();  // Método que implementamos abajo
        });

        btnVerPedidos.setOnClickListener(v -> {
            // Abrir pantalla con lista de pedidos
            Intent intent = new Intent(PedidoActivity.this, ListaPedidosActivity.class);
            startActivity(intent);
        });
    }

    private void actualizarTotal() {
        double subtotal = 0;
        for (Producto p : productosSeleccionados) {
            subtotal += p.getPrecio();
        }
        double impuesto = subtotal * 0.15;
        double total = subtotal + impuesto;
        tvTotal.setText(String.format("Total: L %.2f (incluye 15%% impuesto)", total));
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

    // Simulación guardar pedido en BD o API
    private void guardarPedido() {
        double subtotal = 0;
        for (Producto p : productosSeleccionados) {
            subtotal += p.getPrecio();
        }
        double impuesto = subtotal * 0.15;
        double total = subtotal + impuesto;

        // Aquí tendrías que crear un objeto Pedido con datos reales y llamar a la BD o API REST

        // Simulación:
        Toast.makeText(this, "Pedido guardado!\nSubtotal: L " + subtotal + "\nTotal: L " + total, Toast.LENGTH_LONG).show();

        // Limpias lista y actualizas UI
        productosSeleccionados.clear();
        seleccionadoAdapter.notifyDataSetChanged();
        actualizarTotal();
    }
}
