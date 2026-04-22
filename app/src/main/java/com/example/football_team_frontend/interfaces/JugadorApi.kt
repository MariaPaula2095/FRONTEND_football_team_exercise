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

    @GET("/api/jugadores/listar")
    suspend fun obtenerJugadores(): List<Jugador>

    @POST("/api/jugadores/guardar")
    suspend fun guardarJugador(@Body jugador: Jugador): Jugador

    @DELETE("/api/jugadores/eliminar/{id}")
    suspend fun eliminarJugador(@Path("id") id: Long)

    @PUT("/api/jugadores/actualizar/{id}")
    suspend fun actualizarJugador(
        @Path("id") id: Long,
        @Body jugador: Jugador
    ): Jugador

    @GET("/api/jugadores/equipo/{equipoId}")
    suspend fun obtenerJugadoresPorEquipo(@Path("equipoId") equipoId: Int): List<Jugador>

    @GET("/api/jugadores/goles-mayores")
    suspend fun obtenerJugadoresConGolesMayores(@Query("minGoles") minGoles: Int): List<Jugador>
}