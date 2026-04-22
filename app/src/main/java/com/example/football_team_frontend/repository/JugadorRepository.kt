package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.Jugador

class JugadorRepository {

    private val api = RetrofitClient.jugadorApi

    // ================= CRUD =================

    suspend fun listar(): List<Jugador> {
        return api.listar()
    }

    suspend fun guardar(jugador: Jugador): Jugador {
        return api.guardar(jugador)
    }

    suspend fun actualizar(id: Long, jugador: Jugador): Jugador {
        return api.actualizar(id, jugador)
    }

    suspend fun eliminar(id: Long) {
        api.eliminar(id)
    }

    // ================= CONSULTAS NATIVAS =================

    // Equivale a: findJugadoresByEquipoId

    suspend fun jugadoresPorEquipo(equipoId: Long): List<Jugador> {
        return api.jugadoresPorEquipo(equipoId)
    }

    // Equivale a: findJugadoresConGolesMayoresA

    suspend fun jugadoresConMasGoles(minGoles: Int): List<Jugador> {
        return api.jugadoresConMasGoles(minGoles)
    }
}