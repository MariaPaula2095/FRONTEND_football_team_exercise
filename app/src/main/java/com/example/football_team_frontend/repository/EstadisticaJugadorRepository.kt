package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.EstadisticasJugadorDto
import com.example.football_team_frontend.model.Jugador

class EstadisticaJugadorRepository {

    suspend fun obtenerEstadisticas(): List<EstadisticasJugadorDto> {
        return RetrofitClient.estadisticaJugadorApi.obtenerEstadisticas()
    }

    suspend fun guardarEstadistica(dto: EstadisticasJugadorDto): EstadisticasJugadorDto {
        return RetrofitClient.estadisticaJugadorApi.guardarEstadistica(dto)
    }

    suspend fun actualizarEstadistica(id: Long, dto: EstadisticasJugadorDto): EstadisticasJugadorDto {
        return RetrofitClient.estadisticaJugadorApi.actualizarEstadistica(id, dto)
    }

    suspend fun eliminarEstadistica(id: Long) {
        RetrofitClient.estadisticaJugadorApi.eliminarEstadistica(id)
    }

    suspend fun totalGolesEquipo(idEquipo: Int): Int {
        return RetrofitClient.estadisticaJugadorApi.totalGolesEquipo(idEquipo)
    }

    suspend fun jugadoresConMasGoles(minGoles: Int): List<Jugador> {
        return RetrofitClient.jugadorApi.jugadoresConMasGoles(minGoles)
    }
}