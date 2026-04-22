package com.example.football_team_frontend.interfaces

import com.example.football_team_frontend.model.Partido
import com.example.football_team_frontend.model.ResultadoPartidoDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PartidoApi {

    @GET("/api/partidos/listar")
    suspend fun obtenerPartidos(): List<Partido>

    @POST("/api/partidos/guardar")
    suspend fun guardarPartido(@Body partido: Partido): Partido

    @DELETE("/api/partidos/eliminar/{id}")
    suspend fun eliminarPartido(@Path("id") id: Long)

    @PUT("/api/partidos/actualizar/{id}")
    suspend fun actualizarPartido(
        @Path("id") id: Long,
        @Body partido: Partido
    ): Partido

    @GET("/api/partidos/total-goles/{equipoId}")
    suspend fun obtenerTotalGolesEquipo(@Path("equipoId") equipoId: Int): Int

    @GET("/api/partidos/resultados")
    suspend fun obtenerResultados(): List<ResultadoPartidoDTO>
}