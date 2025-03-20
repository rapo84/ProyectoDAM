package com.example.m13actividad2.network;

import com.example.m13actividad2.interfaces.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
ESTA CLASE ES PARA PODER CONFIGURAR LA CONEXION DEL RETROFIT CON EL SERVIDOR NODE
 */
public class RetrofitClient {
    private static final String BASE_URL = "https://europe-west1-easyorder-grupo07.cloudfunctions.net/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
