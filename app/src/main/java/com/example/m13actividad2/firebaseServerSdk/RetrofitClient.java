package com.example.m13actividad2.firebaseServerSdk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
ESTA CLASE ES PARA PODER CONFIGURAR LA CONEXION DEL RETROFIT CON EL SERVIDOR NODE
 */
public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.56.1:3000";
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
