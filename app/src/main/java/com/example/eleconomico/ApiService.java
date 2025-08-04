package com.example.eleconomico;

import com.example.eleconomico.Usuario;
import com.example.eleconomico.Pedido;
import com.example.eleconomico.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Usuarios
    @POST("usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    @POST("usuarios/register")
    Call<Usuario> register(@Body Usuario usuario);

    // Productos
    @GET("productos")
    Call<List<Producto>> getProductos();

    // Pedidos
    @POST("pedidos")
    Call<Pedido> crearPedido(@Body Pedido pedido);

    @GET("pedidos/usuario/{idUsuario}")
    Call<List<Pedido>> getPedidosPorUsuario(@Path("idUsuario") String idUsuario);

    // etc. según tu API...
}
