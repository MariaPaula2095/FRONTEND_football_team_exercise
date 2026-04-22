package com.example.football_team_frontend.model

import java.util.Date

data class Partido (
    val idPartido: Long=0,
    val fecha: Date,
    val estadio: String,
    val equipoLocal: Equipo,
    val equipoVisita: Equipo,
    val golesLocal: Int,
    val golesVisita: Int,
)

/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPartido;

    private LocalDate fecha;
    private String estadio;

    @ManyToOne
    @JoinColumn(name = "equipo_local")
    private Equipo equipoLocal;

    @ManyToOne
    @JoinColumn(name = "equipo_visita")
    private Equipo equipoVisita;

    private Integer golesLocal;
    private Integer golesVisita;
 */