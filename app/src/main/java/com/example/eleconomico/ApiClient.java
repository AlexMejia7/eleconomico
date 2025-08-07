package com.example.eleconomico;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    //private static final String BASE_URL = "http://34.31.145.38/api/";  // Cambiado a IP de tu instancia
    //private static final String BASE_URL = "http:// 192.168.175.202/api/";
   // 192.168.0.6
    private static final String BASE_URL = "http://192.168.0.6/economico/";

    public static Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


