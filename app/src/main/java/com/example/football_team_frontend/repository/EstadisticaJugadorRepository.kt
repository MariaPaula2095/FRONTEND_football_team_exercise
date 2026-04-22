package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.EstadisticaJugador

class EstadisticaJugadorRepository {

    suspend fun obtenerEstadisticas(): List<EstadisticaJugador> {
        return RetrofitClient.estadisticaJugadorApi.obtenerEstadisticas()
    }

    suspend fun guardarEstadistica(estadisticaJugador: EstadisticaJugador): EstadisticaJugador {
        return RetrofitClient.estadisticaJugadorApi.guardarEstadistica(estadisticaJugador)
    }

    suspend fun totalGolesEquipo(idEquipo: Int): Int {
        return RetrofitClient.estadisticaJugadorApi.totalGolesEquipo(idEquipo)
    }
}