package com.example.m13actividad2.firebaseServerSdk;
/*
 interfaz usada para hacer la llamada al sevidor node para realizar accoones que solo podemos hacerlos por este medio
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Método para asignar un rol a un usuario
    @POST("/setRole")
    Call<ApiResponse> setRole(@Body RoleRequest roleRequest);

    // Método para obtener los claims de un usuario
    @POST("/getClaims")
    Call<ClaimsResponse> getClaims(@Body TokenRequest tokenRequest);

    // Método para asignar un rol a un SuperUsuario
    @POST("/setSuperRole")
    Call<ApiResponse> setSuperRole(@Body RoleRequestAdmin roleRequestAdmin);

    // Método para obtener los claims de un usuario
    @POST("/getClaimsSuper")
    Call<ClaimsResponse> getSuperClaims(@Body TokenRequest tokenRequestAdmin);

    // Método para eliminar a un usuario de los usuarios registrados en firebase auth
    @DELETE("eliminarUsuario/{uid}")
    Call<Void> eliminarUsuario(@Path("uid") String uid);
}
