package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.Jugador

class JugadorRepository {

    suspend fun obtenerJugadores(): List<Jugador> {
        return RetrofitClient.jugadorApi.obtenerJugadores()
    }

    suspend fun guardarJugador(jugador: Jugador): Jugador {
        return RetrofitClient.jugadorApi.guardarJugador(jugador)
    }

    suspend fun eliminarJugador(id: Long) {
        RetrofitClient.jugadorApi.eliminarJugador(id)
    }

    suspend fun actualizarJugador(id: Long, jugador: Jugador): Jugador {
        return RetrofitClient.jugadorApi.actualizarJugador(id, jugador)
    }

    suspend fun obtenerJugadoresPorEquipo(equipoId: Int): List<Jugador> {
        return RetrofitClient.jugadorApi.obtenerJugadoresPorEquipo(equipoId)
    }

    suspend fun obtenerJugadoresConGolesMayores(minGoles: Int): List<Jugador> {
        return RetrofitClient.jugadorApi.obtenerJugadoresConGolesMayores(minGoles)
    }
}