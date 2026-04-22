package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.Partido
import com.example.football_team_frontend.model.ResultadoPartidoDTO

class PartidoRepository {

    private val api = RetrofitClient.partidoApi

    // ================= CRUD =================

    suspend fun listar(): List<Partido> {
        return api.listar()
    }

    suspend fun guardar(partido: Partido): Partido {
        return api.guardar(partido)
    }

    suspend fun actualizar(id: Long, partido: Partido): Partido {
        return api.actualizar(id, partido)
    }

    suspend fun eliminar(id: Long) {
        api.eliminar(id)
    }

    // ================= CONSULTAS NATIVAS =================

    // Equivale a: findTotalGolesByEquipoId
    suspend fun totalGolesPorEquipo(equipoId: Long): Int {
        return api.totalGolesPorEquipo(equipoId)
    }

    // Equivale a: findResultadosConNombresEquipos
    suspend fun obtenerResultados(): List<ResultadoPartidoDTO> {
        return api.obtenerResultados()
    }
}