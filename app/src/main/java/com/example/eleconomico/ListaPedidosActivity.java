package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPedidos;
    private PedidoAdapter pedidoAdapter;

    private List<Pedido> listaPedidos = new ArrayList<>();
    private List<Repartidor> listaRepartidores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        cargarDatosDemo();

        pedidoAdapter = new PedidoAdapter(this, listaPedidos, listaRepartidores, (pedido, repartidor) -> {
            Toast.makeText(this, "Repartidor " + repartidor.getNombre() + " asignado a Pedido #" + pedido.getIdPedido(), Toast.LENGTH_SHORT).show();

            // Aquí agregar lógica para actualizar el pedido con repartidor en BD o API
        });

        recyclerViewPedidos.setAdapter(pedidoAdapter);
    }

    private void cargarDatosDemo() {
        // Crear lista demo de productos para usar en pedidos
        List<Producto> productosDemo = new ArrayList<>();
        productosDemo.add(new Producto("1", "Producto Demo 1", 100.0));
        productosDemo.add(new Producto("2", "Producto Demo 2", 150.0));

        listaPedidos.add(new Pedido(
                "1",          // idPedido
                "U1",         // idUsuario
                "Juan Pérez", // nombreUsuario
                productosDemo,
                "Pendiente",  // estado
                "2025-08-07", // fecha
                250.0         // total
        ));

        listaPedidos.add(new Pedido(
                "2", "U2", "María López", productosDemo, "Enviado", "2025-08-06", 300.0
        ));

        listaPedidos.add(new Pedido(
                "3", "U3", "Carlos Gómez", productosDemo, "Entregado", "2025-08-05", 450.0
        ));

        listaRepartidores.add(new Repartidor("R1", "Juan Pérez"));
        listaRepartidores.add(new Repartidor("R2", "María Gómez"));
        listaRepartidores.add(new Repartidor("R3", "Carlos López"));
    }
}
