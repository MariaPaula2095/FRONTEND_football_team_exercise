package com.example.football_team_frontend.model

import java.util.Date

data class ResultadoPartidoDTO (
    val idPartido: Int,
    val fecha: Date,
    val estadio: String,
    val equipoLocal: String,
    val golesLocal: Int,
    val equipoVisitante: String,
    val golesVisitante: Int
)
