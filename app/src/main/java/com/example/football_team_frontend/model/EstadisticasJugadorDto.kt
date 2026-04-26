package com.example.football_team_frontend.model

data class EstadisticasJugadorDto(
    val idEstadistica: Long? = null,
    val idJugador: Long? = null,
    val nombreJugador: String? = null,
    val idPartido: Long? = null,
    val minutosJugados: Int? = null,
    val goles: Int? = null,
    val asistencias: Int? = null,
    val tarjetasAmarillas: Int? = null,
    val tarjetasRojas: Int? = null
)