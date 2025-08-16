package com.example.eleconomico;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;  // <-- Import necesario

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    // Login con JSON genérico
    @POST("login.php")
    Call<JsonObject> loginRaw(@Body JsonObject json);

    // Registro usuario con clase Usuario (debes tener esta clase bien definida)
    @POST("registro_usuario.php")
    Call<Mensaje> registrarUsuario(@Body Usuario usuario);

    // Obtener lista de productos
    @GET("productos.php")
    Call<List<Producto>> getProductos();

    // Guardar pedido (revisar que envíes bien el RequestBody)
    @POST("pedidos.php")
    Call<JsonObject> guardarPedido(@Body okhttp3.RequestBody body);

    // Obtener pedidos por usuario
    @GET("pedidos.php")
    Call<List<Pedido>> getPedidosPorUsuario(@Query("idUsuario") String idUsuario);

    // Obtener repartidores
    @GET("repartidores.php")
    Call<List<Repartidor>> getRepartidores();

    // Crear repartidor con clase o Map (mejor clase si puedes)
    @POST("crear_repartidor.php")
    Call<Mensaje> crearRepartidor(@Body Map<String, String> body);

    // Obtener pedidos asignados a repartidor
    @GET("pedidos_repartidor.php")
    Call<List<Pedido>> getPedidosAsignados(@Query("idRepartidor") String idRepartidor);

    // Actualizar estado de pedido asignado
    @PUT("pedidos_repartidor.php")
    Call<Mensaje> actualizarEstadoPedido(@Body Map<String, String> body);
}
