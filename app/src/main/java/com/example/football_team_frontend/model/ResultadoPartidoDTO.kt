package com.example.football_team_frontend.model

import java.util.Date

data class ResultadoPartido(
    val idPartido: Long? = null,
    val fecha: String = "",
    val estadio: String = "",
    val equipoLocal: String = "",
    val golesLocal: Int = 0,
    val equipoVisitante: String = "",
    val golesVisitante: Int = 0
)
