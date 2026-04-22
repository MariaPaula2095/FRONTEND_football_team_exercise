package com.example.football_team_frontend.repository

import com.example.football_team_frontend.interfaces.RetrofitClient
import com.example.football_team_frontend.model.Equipo

class EquipoRepository {

    suspend fun obtenerEquipos(): List<Equipo> {
        return RetrofitClient.equipoApi.obtenerEquipos()
    }

    suspend fun guardarEquipo(equipo: Equipo): Equipo {
        return RetrofitClient.equipoApi.guardarEquipo(equipo)
    }

    suspend fun eliminarEquipo(id: Long) {
        RetrofitClient.equipoApi.eliminarEquipo(id)
    }

    suspend fun actualizarEquipo(id: Long, equipo: Equipo): Equipo {
        return RetrofitClient.equipoApi.actualizarEquipo(id, equipo)
    }
}