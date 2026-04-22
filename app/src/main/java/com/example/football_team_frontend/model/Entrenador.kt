package com.example.football_team_frontend.model

import java.util.Date

data class Entrenador (
    val idEntrenador: Long=0,
    val nombre: String,
    val especialidad: String,
    val equipo: Equipo
)
/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEntrenador;

    private String nombre;
    private String especialidad;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;
 */