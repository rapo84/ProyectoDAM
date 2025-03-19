package com.example.m13actividad2.firebaseServerSdk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Método para asignar un rol a un usuario
    @POST("https://europe-west1-easyorder-grupo07.cloudfunctions.net/setRole")
    Call<ApiResponse> setRole(@Body RoleRequest roleRequest);

    // Método para obtener los claims de un usuario
    @POST("https://europe-west1-easyorder-grupo07.cloudfunctions.net/getClaims")
    Call<ClaimsResponse> getClaims(@Body TokenRequest tokenRequest);

    // Método para asignar un rol a un SuperUsuario
    @POST("https://europe-west1-easyorder-grupo07.cloudfunctions.net/setSuperRole")
    Call<ApiResponse> setSuperRole(@Body RoleRequestAdmin roleRequestAdmin);

    // Método para obtener los claims de un SuperUsuario
    @POST("https://europe-west1-easyorder-grupo07.cloudfunctions.net/getClaimsSuper")
    Call<ClaimsResponse> getSuperClaims(@Body TokenRequest tokenRequestAdmin);

    // Método para eliminar a un usuario de los usuarios registrados en firebase auth
    @DELETE("https://europe-west1-easyorder-grupo07.cloudfunctions.net/eliminarUsuario/{uid}")
    Call<Void> eliminarUsuario(@Path("uid") String uid);
}

