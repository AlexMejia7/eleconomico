package com.example.eleconomico;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // --- USUARIOS ---

    @POST("usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    @POST("usuarios/register")
    Call<Usuario> register(@Body Usuario usuario);

    // --- PRODUCTOS ---

    @GET("productos")
    Call<List<Producto>> getProductos();

    // --- PEDIDOS ---

    @POST("pedidos")
    Call<Pedido> crearPedido(@Body Pedido pedido);

    @GET("pedidos/usuario/{idUsuario}")
    Call<List<Pedido>> getPedidosPorUsuario(@Path("idUsuario") String idUsuario);

}
