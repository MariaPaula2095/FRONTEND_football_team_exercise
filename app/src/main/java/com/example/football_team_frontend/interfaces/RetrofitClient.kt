package com.example.football_team_frontend.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
RetrofitClient crea la conexión con el backend
Define la URL base (http://10.0.2.2:8080)
Configura Retrofit
Genera la instancia (entrenadorApi) para llamar a la API
 */
object RetrofitClient {
    private const val BASE_URL = "http://192.168.18.17:8080/"

    //API DE ENTRENADOR
    val entrenadorApi: EntrenadorApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EntrenadorApi::class.java)
    }

    //API DE EQUIPO
    val equipoApi: EquipoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EquipoApi::class.java)
    }

    //API DE ESTADISTICA JUGADOR
    val estadisticaJugadorApi: EstadisticaJugadorApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EstadisticaJugadorApi::class.java)
    }

    //API DE JUGADOR
    val jugadorApi: JugadorApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JugadorApi::class.java)
    }

    //API DE PARTIDO
    val partidoApi: PartidoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PartidoApi::class.java)
    }
}