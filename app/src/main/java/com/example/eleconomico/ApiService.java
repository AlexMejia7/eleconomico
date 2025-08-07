package com.example.eleconomico;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject; // Añadido para loginRaw
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // USUARIOS
    @POST("usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    // Método raw (crudo) para login con JsonObject si prefieres manejar el JSON directamente
    @POST("usuarios/login")
    Call<JsonObject> loginRaw(@Body JsonObject body);

    @POST("usuarios/register")
    Call<Mensaje> registrarUsuario(@Body Usuario usuario);

    // PRODUCTOS
    @GET("productos")
    Call<List<Producto>> getProductos();

    // PEDIDOS
    @POST("pedidos")
    Call<Pedido> crearPedido(@Body Pedido pedido);

    @GET("pedidos/usuario/{idUsuario}")
    Call<List<Pedido>> getPedidosPorUsuario(@Path("idUsuario") String idUsuario);

    // REPARTIDORES - PEDIDOS ASIGNADOS
    @GET("pedidos_repartidor")
    Call<List<Pedido>> getPedidosAsignados(@Query("idRepartidor") String idRepartidor);

    // REPARTIDORES - ACTUALIZAR ESTADO PEDIDO
    @PUT("pedidos_repartidor")
    Call<Mensaje> actualizarEstadoPedido(@Body Map<String, String> body);
}
