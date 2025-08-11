package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPedidos;
    private PedidoAdapter pedidoAdapter;

    private List<Pedido> listaPedidos = new ArrayList<>();
    private List<Repartidor> listaRepartidores = new ArrayList<>();

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        pedidoAdapter = new PedidoAdapter(this, listaPedidos, listaRepartidores, (pedido, repartidor) -> {
            Toast.makeText(this, "Repartidor " + repartidor.getNombre() + " asignado a Pedido #" + pedido.getIdPedido(), Toast.LENGTH_SHORT).show();

            // Aquí agregar lógica para actualizar el pedido con repartidor en BD o API
        });

        recyclerViewPedidos.setAdapter(pedidoAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        cargarPedidosDesdeApi();
    }

    private void cargarPedidosDesdeApi() {
        String idUsuario = sessionManager.getUserId(); // Asumiendo que tienes este método

        if (idUsuario == null) {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<Pedido>> call = apiService.getPedidosPorUsuario(idUsuario);
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPedidos.clear();
                    listaPedidos.addAll(response.body());
                    pedidoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListaPedidosActivity.this, "No se encontraron pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(ListaPedidosActivity.this, "Error al cargar pedidos: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
