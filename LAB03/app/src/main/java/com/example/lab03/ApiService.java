package com.example.lab03;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.lab03.bean.Respuestas;

public interface ApiService {

    @GET("api.php")
    Call<Respuestas> obtenerPreguntas(
            @Query("amount") int cantidad,
            @Query("difficulty") String dificultad,
            @Query("type") String tipo
    );
}
