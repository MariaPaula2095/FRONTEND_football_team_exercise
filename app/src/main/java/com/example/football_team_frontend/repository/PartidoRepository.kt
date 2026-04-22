package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.Partido
import com.example.football_team_frontend.model.ResultadoPartidoDTO

class PartidoRepository {

    suspend fun obtenerPartidos(): List<Partido> {
        return RetrofitClient.partidoApi.obtenerPartidos()
    }

    suspend fun guardarPartido(partido: Partido): Partido {
        return RetrofitClient.partidoApi.guardarPartido(partido)
    }

    suspend fun eliminarPartido(id: Long) {
        RetrofitClient.partidoApi.eliminarPartido(id)
    }

    suspend fun actualizarPartido(id: Long, partido: Partido): Partido {
        return RetrofitClient.partidoApi.actualizarPartido(id, partido)
    }

    suspend fun obtenerTotalGolesEquipo(equipoId: Int): Int {
        return RetrofitClient.partidoApi.obtenerTotalGolesEquipo(equipoId)
    }

    suspend fun obtenerResultados(): List<ResultadoPartidoDTO> {
        return RetrofitClient.partidoApi.obtenerResultados()
    }
}