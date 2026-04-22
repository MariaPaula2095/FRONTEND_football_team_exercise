package com.example.football_team_frontend.model

import java.util.Date

data class Equipo (
    val idEquipo: Long=0,
    val nombre: String,
    val ciudad: String,
    val fundacion: Date
)

/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipo;
    private String nombre;
    private String ciudad;
    private LocalDate fundacion;
 */