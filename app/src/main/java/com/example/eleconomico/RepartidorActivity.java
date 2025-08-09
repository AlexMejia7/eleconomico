package com.example.eleconomico;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepartidorActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPedidos;
    private PedidoAdapter pedidoAdapter;
    private ApiService apiService;
    private String idRepartidor = "1"; // Id repartidor para pruebas

    private List<Repartidor> repartidores;  // Lista de repartidores

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient().create(ApiService.class);

        cargarRepartidores(); // Primero carga la lista de repartidores
    }

    private void cargarRepartidores() {
        apiService.getRepartidores().enqueue(new Callback<List<Repartidor>>() {
            @Override
            public void onResponse(Call<List<Repartidor>> call, Response<List<Repartidor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repartidores = response.body();
                    cargarPedidos(); // Carga pedidos después de tener repartidores
                } else {
                    Toast.makeText(RepartidorActivity.this, "Error al cargar repartidores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repartidor>> call, Throwable t) {
                Toast.makeText(RepartidorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarPedidos() {
        apiService.getPedidosAsignados(idRepartidor).enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidos = response.body();

                    pedidoAdapter = new PedidoAdapter(
                            RepartidorActivity.this,
                            pedidos,
                            repartidores,
                            (pedido, repartidorSeleccionado) -> {
                                // Aquí usas pedido y repartidorSeleccionado
                                actualizarEstadoPedido(pedido.getIdPedido(), "Entregado");
                            }
                    );

                    recyclerViewPedidos.setAdapter(pedidoAdapter);

                } else {
                    Toast.makeText(RepartidorActivity.this, "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(RepartidorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarEstadoPedido(String idPedido, String nuevoEstado) {
        Map<String, String> body = new HashMap<>();
        body.put("idPedido", idPedido);
        body.put("estado", nuevoEstado);

        apiService.actualizarEstadoPedido(body).enqueue(new Callback<Mensaje>() {
            @Override
            public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RepartidorActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    cargarPedidos(); // Refrescar lista tras actualizar
                } else {
                    Toast.makeText(RepartidorActivity.this, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mensaje> call, Throwable t) {
                Toast.makeText(RepartidorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
