package com.example.football_team_frontend.model

import java.util.Date


data class Jugador(
    val idJugador: Long? = null,
    val nombre: String = "",
    val posicion: String = "",
    val dorsal: Int = 0,
    val fechaNac: String = "",   // "YYYY-MM-DD"
    val nacionalidad: String = "",
    val idEquipo: Long? = null   // ← DTO usa idEquipo, no objeto Equipo
)
/*
    ATRIBUTOS EN BACKEND
    private Long idJugador;
    private String nombre;
    private String posicion;
    private Integer dorsal;
    private LocalDate fechaNac;
    private String nacionalidad;
    private Long idEquipo;
 */