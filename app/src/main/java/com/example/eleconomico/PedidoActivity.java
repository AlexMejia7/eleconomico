package com.example.eleconomico;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoActivity extends AppCompatActivity {

    private Button btnGuardarPedido, btnActualizarPedido, btnEliminarPedido, btnVerPedidos;
    private RecyclerView recyclerViewProductos, recyclerViewSeleccionados;
    private ProductoAdapter productoAdapter, seleccionadoAdapter;
    private TextView tvTotal;
    private Spinner spinnerRepartidores;

    private List<Producto> productosDisponibles = new ArrayList<>();
    private List<Producto> productosSeleccionados = new ArrayList<>();
    private List<Repartidor> repartidores = new ArrayList<>();

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        // Inicialización
        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);

        btnGuardarPedido = findViewById(R.id.btnGuardarPedido);
        btnActualizarPedido = findViewById(R.id.btnActualizarPedido);
        btnEliminarPedido = findViewById(R.id.btnEliminarPedido);
        btnVerPedidos = findViewById(R.id.btnVerPedidos);

        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        recyclerViewSeleccionados = findViewById(R.id.recyclerViewSeleccionados);
        tvTotal = findViewById(R.id.tvTotal);
        spinnerRepartidores = findViewById(R.id.spinnerRepartidores);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSeleccionados.setLayoutManager(new LinearLayoutManager(this));

        productosDisponibles = obtenerProductosDemo();

        cargarRepartidores();

        // Adapter para productos disponibles
        productoAdapter = new ProductoAdapter(productosDisponibles, producto -> {
            if (!productosSeleccionados.contains(producto)) {
                producto.setCantidad(1);
                productosSeleccionados.add(producto);
                seleccionadoAdapter.notifyDataSetChangedSafe();
                actualizarTotal();
                Toast.makeText(this, producto.getNombre() + " agregado al pedido", Toast.LENGTH_SHORT).show();
            }
        });

        // Adapter para productos seleccionados
        seleccionadoAdapter = new ProductoAdapter(productosSeleccionados, producto -> {
            productosSeleccionados.remove(producto);
            seleccionadoAdapter.notifyDataSetChangedSafe();
            actualizarTotal();
            Toast.makeText(this, producto.getNombre() + " removido del pedido", Toast.LENGTH_SHORT).show();
        });

        recyclerViewProductos.setAdapter(productoAdapter);
        recyclerViewSeleccionados.setAdapter(seleccionadoAdapter);

        // Botones
        btnGuardarPedido.setOnClickListener(v -> {
            if (productosSeleccionados.isEmpty()) {
                Toast.makeText(this, "No hay productos para guardar", Toast.LENGTH_SHORT).show();
                return;
            }
            Repartidor repartidorSeleccionado = (Repartidor) spinnerRepartidores.getSelectedItem();
            if (repartidorSeleccionado == null || repartidorSeleccionado.getIdRepartidor() == null ||
                    repartidorSeleccionado.getIdRepartidor().trim().isEmpty()) {
                Toast.makeText(this, "Repartidor no seleccionado o ID vacío", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG_PEDIDO", "Repartidor seleccionado: " + repartidorSeleccionado);
                return;
            }
            guardarPedido();
        });

        btnActualizarPedido.setOnClickListener(v -> Toast.makeText(this, "Función actualizar en construcción", Toast.LENGTH_SHORT).show());
        btnEliminarPedido.setOnClickListener(v -> Toast.makeText(this, "Función eliminar en construcción", Toast.LENGTH_SHORT).show());
        btnVerPedidos.setOnClickListener(v -> Toast.makeText(this, "Función ver pedidos en construcción", Toast.LENGTH_SHORT).show());

        actualizarTotal();
    }

    private void cargarRepartidores() {
        apiService.getRepartidores().enqueue(new Callback<List<Repartidor>>() {
            @Override
            public void onResponse(Call<List<Repartidor>> call, Response<List<Repartidor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    repartidores = response.body();
                    if (repartidores.isEmpty()) {
                        repartidores.add(new Repartidor("0", "Repartidor Genérico"));
                    }
                    ArrayAdapter<Repartidor> adapter = new ArrayAdapter<>(
                            PedidoActivity.this,
                            android.R.layout.simple_spinner_item,
                            repartidores
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerRepartidores.setAdapter(adapter);
                    spinnerRepartidores.setSelection(0);
                } else {
                    Toast.makeText(PedidoActivity.this, "Error al cargar repartidores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repartidor>> call, Throwable t) {
                Toast.makeText(PedidoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarTotal() {
        double subtotal = 0;
        for (Producto p : productosSeleccionados) {
            int cantidad = p.getCantidad() > 0 ? p.getCantidad() : 1;
            subtotal += p.getPrecio() * cantidad;
        }
        double impuesto = subtotal * 0.15;
        double total = subtotal + impuesto;
        tvTotal.setText(String.format(Locale.getDefault(), "Total: L %.2f (incluye 15%% impuesto)", total));
    }

    private List<Producto> obtenerProductosDemo() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto(1, "Six Pack Corona (2 unidades)", 800.00));
        lista.add(new Producto(2, "Pañales para bebé 45 unidades", 750.00));
        lista.add(new Producto(3, "Leche Ceteco 500g", 450.00));
        lista.add(new Producto(4, "10 Libras de Chuleta", 380.00));
        lista.add(new Producto(5, "Cerveza", 15.50));
        return lista;
    }

    private void guardarPedido() {
        String idUsuario = sessionManager.getUserId();
        if (idUsuario == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        Repartidor repartidorSeleccionado = (Repartidor) spinnerRepartidores.getSelectedItem();
        String idRepartidor = repartidorSeleccionado.getIdRepartidor();
        Log.d("DEBUG_PEDIDO", "ID Repartidor: " + idRepartidor);

        List<Map<String, Object>> productosParaEnviar = new ArrayList<>();
        for (Producto p : productosSeleccionados) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", p.getId_producto());
            item.put("cantidad", p.getCantidad() > 0 ? p.getCantidad() : 1);
            productosParaEnviar.add(item);
        }

        String ubicacionEntrega = "15.5040,-88.0256"; // Hardcodeada temporal

        Map<String, Object> pedidoMap = new HashMap<>();
        pedidoMap.put("id_usuario", Integer.parseInt(idUsuario));
        pedidoMap.put("productos", productosParaEnviar);
        pedidoMap.put("ubicacion_entrega", ubicacionEntrega);
        pedidoMap.put("id_repartidor", Integer.parseInt(idRepartidor));

        Gson gson = new Gson();
        String jsonPedido = gson.toJson(pedidoMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonPedido);

        apiService.guardarPedido(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productosSeleccionados.clear();
                    seleccionadoAdapter.notifyDataSetChangedSafe();
                    actualizarTotal();
                    Toast.makeText(PedidoActivity.this, "Pedido guardado correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PedidoActivity.this, "Error al guardar pedido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PedidoActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
