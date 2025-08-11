package com.example.eleconomico;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // Instancia singleton volatile para seguridad en hilos
    private static volatile Retrofit retrofit = null;

    // Base URL apuntando a tu servidor con IP pública
    private static final String BASE_URL = "http://34.31.145.38/";

    // Método para obtener la instancia Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    // Opcional: método para reiniciar la instancia si necesitas cambiar la URL
    public static void resetClient() {
        retrofit = null;
    }
}
