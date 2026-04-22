package com.example.football_team_frontend.interfaces

import com.example.football_team_frontend.model.Entrenador
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EntrenadorApi {

    @GET("/api/entrenadores")
    suspend fun obtenerEntrenadores(): List<Entrenador>

    @GET("/api/entrenadores/{id}")
    suspend fun obtenerEntrenador(@Path("id") id: Long): Entrenador

    @POST("/api/entrenadores")
    suspend fun guardarEntrenador(@Body entrenador: Entrenador): Entrenador

    @DELETE("/api/entrenadores/{id}")
    suspend fun eliminarEntrenador(@Path("id") id: Long)
}