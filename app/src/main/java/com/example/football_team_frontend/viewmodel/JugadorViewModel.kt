package com.example.football_team_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.repository.JugadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JugadorViewModel : ViewModel() {

    private val repository = JugadorRepository()

    // ================= ESTADOS =================

    private val _jugadores = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadores: StateFlow<List<Jugador>> = _jugadores

    private val _jugadoresEquipo = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadoresEquipo: StateFlow<List<Jugador>> = _jugadoresEquipo

    private val _jugadoresGoles = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadoresGoles: StateFlow<List<Jugador>> = _jugadoresGoles

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

    // ================= CRUD =================

    fun listar() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _jugadores.value = repository.listar()
            } catch (e: Exception) {
                _mensaje.value = "Error de conexión: Verifica tu internet o el estado del servidor."
                println("DEBUG: ${e.localizedMessage}")
            } finally {
                _cargando.value = false
            }
        }
    }

    fun guardar(jugador: Jugador) {
        viewModelScope.launch {
            _cargando.value = true
            _guardadoExitoso.value = false
            try {
                repository.guardar(jugador)
                _mensaje.value = "Jugador guardado correctamente"
                listar() // refresca lista
                _guardadoExitoso.value = true
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizar(id: Long, jugador: Jugador) {
        viewModelScope.launch {
            _cargando.value = true
            _guardadoExitoso.value = false
            try {
                repository.actualizar(id, jugador)
                _mensaje.value = "Jugador actualizado correctamente"
                listar()
                _guardadoExitoso.value = true
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repository.eliminar(id)
                _mensaje.value = "Jugador eliminado correctamente"
                listar()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    // ================= CONSULTAS NATIVAS =================

    fun jugadoresPorEquipo(equipoId: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _jugadoresEquipo.value = repository.jugadoresPorEquipo(equipoId)
            } catch (e: Exception) {
                _mensaje.value = "Error al buscar por equipo: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun jugadoresConMasGoles(minGoles: Int) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _jugadoresGoles.value = repository.jugadoresConMasGoles(minGoles)
            } catch (e: Exception) {
                _mensaje.value = "Error al buscar por goles: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun resetGuardado() {
        _guardadoExitoso.value = false
    }
}