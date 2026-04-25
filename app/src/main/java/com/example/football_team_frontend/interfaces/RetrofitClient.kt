package com.example.football_team_frontend.interfaces

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
RetrofitClient crea la conexión con el backend
Define la URL base (http://10.0.2.2:8080)
Configura Retrofit con tiempos de espera optimizados
Genera las instancias de API para el proyecto
 */
object RetrofitClient {
    private const val BASE_URL = "http://192.168.10.43:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS) // Tiempo máximo para conectar
        .readTimeout(20, TimeUnit.SECONDS)    // Tiempo máximo para leer datos
        .writeTimeout(20, TimeUnit.SECONDS)   // Tiempo máximo para enviar datos
        .retryOnConnectionFailure(true)      // Reintenta automáticamente si falla la conexión
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val entrenadorApi: EntrenadorApi by lazy { retrofit.create(EntrenadorApi::class.java) }
    val equipoApi: EquipoApi by lazy { retrofit.create(EquipoApi::class.java) }
    val estadisticaJugadorApi: EstadisticaJugadorApi by lazy { retrofit.create(EstadisticaJugadorApi::class.java) }
    val jugadorApi: JugadorApi by lazy { retrofit.create(JugadorApi::class.java) }
    val partidoApi: PartidoApi by lazy { retrofit.create(PartidoApi::class.java) }
}
