package com.example.eleconomico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private List<Repartidor> repartidores;

    private FloatingActionButton fabAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        fabAgregar = findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregarRepartidor());

        apiService = ApiClient.getClient().create(ApiService.class);

        cargarRepartidores();
    }

    private void mostrarDialogoAgregarRepartidor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Repartidor");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_repartidor, null);
        final EditText inputNombre = viewInflated.findViewById(R.id.inputNombre);
        final EditText inputCorreo = viewInflated.findViewById(R.id.inputCorreo);
        final EditText inputContrasena = viewInflated.findViewById(R.id.inputContrasena);

        builder.setView(viewInflated);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = inputNombre.getText().toString().trim();
            String correo = inputCorreo.getText().toString().trim();
            String contrasena = inputContrasena.getText().toString();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(RepartidorActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            agregarRepartidor(nombre, correo, contrasena);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void agregarRepartidor(String nombre, String correo, String contrasena) {
        Map<String, String> nuevoRepartidor = new HashMap<>();
        nuevoRepartidor.put("nombre", nombre);
        nuevoRepartidor.put("correo", correo);
        nuevoRepartidor.put("contrasena", contrasena);

        apiService.crearRepartidor(nuevoRepartidor).enqueue(new Callback<Mensaje>() {
            @Override
            public void onResponse(Call<Mensaje> call, Response<Mensaje> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RepartidorActivity.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    cargarRepartidores();
                } else {
                    Toast.makeText(RepartidorActivity.this, "Error al crear repartidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mensaje> call, Throwable t) {
                Toast.makeText(RepartidorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarRepartidores() {
        apiService.getRepartidores().enqueue(new Callback<List<Repartidor>>() {
            @Override
            public void onResponse(Call<List<Repartidor>> call, Response<List<Repartidor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repartidores = response.body();
                    cargarPedidos();
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
                                actualizarEstadoPedido(pedido.getIdPedido(), "entregado");
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
                    cargarPedidos();
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
