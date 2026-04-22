package com.example.football_team_frontend.interfaces

import com.example.football_team_frontend.model.Equipo
import retrofit2.http.*

interface EquipoApi {

    @GET("/api/equipos/listar")
    suspend fun obtenerEquipos(): List<Equipo>

    @POST("/api/equipos/guardar")
    suspend fun guardarEquipo(@Body equipo: Equipo): Equipo

    @DELETE("/api/equipos/eliminar/{id}")
    suspend fun eliminarEquipo(@Path("id") id: Long)

    @PUT("/api/equipos/actualizar/{id}")
    suspend fun actualizarEquipo(
        @Path("id") id: Long,
        @Body equipo: Equipo
    ): Equipo
}