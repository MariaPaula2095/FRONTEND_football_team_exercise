package com.example.football_team_frontend.model

import java.util.Date

data class Jugador (
    val idJugador: Long=0,
    val nombre: String,
    val posicion: String,
    val dorsal: Int,
    val fechaNac: Date
)
/*
    ATRIBUTOS EN BACKEND
    private Long idJugador;
    private String nombre;
    private String posicion;
    private Integer dorsal;
    private LocalDate fechaNac;
    private String nacionalidad;
 */