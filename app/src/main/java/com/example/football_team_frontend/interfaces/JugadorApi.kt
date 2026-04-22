package com.example.football_team_frontend.interfaces
import com.example.football_team_frontend.model.Jugador
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JugadorApi {

    // ================= CRUD =================

    @GET("/api/jugadores/listar")
    suspend fun listar(): List<Jugador>

    @POST("/api/jugadores/guardar")
    suspend fun guardar(@Body jugador: Jugador): Jugador

    @PUT("/api/jugadores/actualizar/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body jugador: Jugador
    ): Jugador

    @DELETE("/api/jugadores/eliminar/{id}")
    suspend fun eliminar(@Path("id") id: Long)

    // ================= CONSULTAS NATIVAS =================

    @GET("/api/jugadores/equipo/{equipoId}")
    suspend fun jugadoresPorEquipo(
        @Path("equipoId") equipoId: Long
    ): List<Jugador>

    @GET("/api/jugadores/goles-mayores")
    suspend fun jugadoresConMasGoles(
        @Query("minGoles") minGoles: Int
    ): List<Jugador>
}