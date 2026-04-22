package com.example.football_team_frontend.model

import java.util.Date

data class EstadisticaJugador (
    val idEstadistica: Long=0,
    val Jugador: Jugador,
    val partido: Partido,
    val minutosJugados: Int,
    val goles: Int,
    val asistencias: Int,
    val tarjetasAmarillas: Int,
    val tarjetasRojas: Int
)

/*
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadistica;

    @ManyToOne
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;

    @ManyToOne
    @JoinColumn(name = "id_partido")
    private Partido partido;

    private Integer minutosJugados;
    private Integer goles;
    private Integer asistencias;
    private Integer tarjetasAmarillas;
    private Integer tarjetasRojas;
 */