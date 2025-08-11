package com.example.eleconomico;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static volatile Retrofit retrofit = null;

    // Base URL con la IP pública de tu servidor (asegúrate que termine con /)
    private static final String BASE_URL = "http://34.31.145.38/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)  // Tiempo para conexión
                            .readTimeout(30, TimeUnit.SECONDS)     // Tiempo para lectura de respuesta
                            .writeTimeout(30, TimeUnit.SECONDS)    // Tiempo para envío de datos
                            .retryOnConnectionFailure(true)        // Reintentar en fallos de conexión
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static void resetClient() {
        retrofit = null;
    }
}
