package com.example.football_team_frontend.interfaces

import com.example.football_team_frontend.model.EstadisticasJugadorDto
import retrofit2.http.*

interface EstadisticaJugadorApi {

    @GET("/api/estadisticas/Listar/partido-jugador")
    suspend fun obtenerEstadisticas(): List<EstadisticasJugadorDto>

    @POST("/api/estadisticas/Guardar/partido-jugador")
    suspend fun guardarEstadistica(@Body dto: EstadisticasJugadorDto): EstadisticasJugadorDto

    @PUT("/api/estadisticas/Actualizar/partido-jugador/{id}")
    suspend fun actualizarEstadistica(
        @Path("id") id: Long,
        @Body dto: EstadisticasJugadorDto
    ): EstadisticasJugadorDto

    @DELETE("/api/estadisticas/Eliminar/partido-jugador/{id}")
    suspend fun eliminarEstadistica(@Path("id") id: Long)

    @GET("/api/estadisticas/total-goles/{idEquipo}")
    suspend fun totalGolesEquipo(@Path("idEquipo") idEquipo: Int): Int
}