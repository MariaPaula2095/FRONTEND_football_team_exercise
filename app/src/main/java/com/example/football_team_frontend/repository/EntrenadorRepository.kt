package com.example.football_team_frontend.repository

import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.interfaces.RetrofitClient

class EntrenadorRepository {

    suspend fun obtenerEntrenadores(): List<Entrenador> {
        return RetrofitClient.entrenadorApi.obtenerEntrenadores()
    }

    suspend fun actualizarEntrenador(id: Long, entrenador: Entrenador): Entrenador {
        return RetrofitClient.entrenadorApi.actualizarEntrenador(id, entrenador)
    }

    suspend fun guardarEntrenador(entrenador: Entrenador): Entrenador {
        return RetrofitClient.entrenadorApi.guardarEntrenador(entrenador)
    }

    suspend fun eliminarEntrenador(id: Long) {
        RetrofitClient.entrenadorApi.eliminarEntrenador(id)
    }
}