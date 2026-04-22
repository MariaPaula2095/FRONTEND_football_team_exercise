package com.example.football_team_frontend.interfaces

import com.example.football_team_frontend.model.EstadisticaJugador
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EstadisticaJugadorApi {

    @GET("/api/estadisticas/listar")
    suspend fun obtenerEstadisticas(): List<EstadisticaJugador>

    @POST("/api/estadisticas/guardar")
    suspend fun guardarEstadistica(@Body estadisticaJugador: EstadisticaJugador): EstadisticaJugador

    @GET("/api/estadisticas/total-goles/{idEquipo}")
    suspend fun totalGolesEquipo(@Path("idEquipo") idEquipo: Int): Int
}