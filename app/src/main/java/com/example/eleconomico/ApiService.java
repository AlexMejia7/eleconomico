package com.example.eleconomico;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    // Usuarios
    @POST("login.php")
    Call<JsonObject> loginRaw(@Body JsonObject json);

    @POST("registro_usuario.php")
    Call<Mensaje> registrarUsuario(@Body Usuario usuario);

    // Productos
    @GET("productos.php")
    Call<List<Producto>> getProductos();

    // Pedidos
    @POST("pedidos.php")
    Call<JsonObject> guardarPedido(@Body okhttp3.RequestBody body);

    @GET("pedidos.php")
    Call<List<Pedido>> getPedidosPorUsuario(@Query("idUsuario") String idUsuario);

    // Repartidores
    @GET("repartidores.php")
    Call<List<Repartidor>> getRepartidores();

    // Crear nuevo repartidor (POST)
    @POST("crear_repartidor.php")
    Call<Mensaje> crearRepartidor(@Body Map<String, String> body);

    // Pedidos asignados a repartidor
    @GET("pedidos_repartidor.php")
    Call<List<Pedido>> getPedidosAsignados(@Query("idRepartidor") String idRepartidor);

    // Actualizar estado pedido repartidor
    @PUT("pedidos_repartidor.php")
    Call<Mensaje> actualizarEstadoPedido(@Body Map<String, String> body);
}
