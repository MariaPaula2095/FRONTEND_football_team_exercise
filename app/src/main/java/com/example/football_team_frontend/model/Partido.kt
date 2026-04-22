package com.example.football_team_frontend.model

import java.util.Date

data class Partido(
    val idPartido: Long? = null,
    val fecha: String = "",      // "YYYY-MM-DD"
    val estadio: String = "",
    val idEquipoLocal: Long? = null,
    val idEquipoVisita: Long? = null,
    val golesLocal: Int = 0,
    val golesVisita: Int = 0
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