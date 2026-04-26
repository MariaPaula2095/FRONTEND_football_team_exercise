package com.example.football_team_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football_team_frontend.model.EstadisticasJugadorDto
import com.example.football_team_frontend.repository.EstadisticaJugadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstadisticaViewModel : ViewModel() {

    private val repository = EstadisticaJugadorRepository()

    private val _estadisticas = MutableStateFlow<List<EstadisticasJugadorDto>>(emptyList())
    val estadisticas: StateFlow<List<EstadisticasJugadorDto>> = _estadisticas

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _totalGolesEquipo = MutableStateFlow<Int>(0)
    val totalGolesEquipo: StateFlow<Int> = _totalGolesEquipo

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

    fun obtenerEstadisticas() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _estadisticas.value = repository.obtenerEstadisticas()
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener estadísticas: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun guardarEstadistica(dto: EstadisticasJugadorDto) {
        viewModelScope.launch {
            _cargando.value = true
            _guardadoExitoso.value = false
            try {
                repository.guardarEstadistica(dto)
                _mensaje.value = "Estadística guardada con éxito"
                _guardadoExitoso.value = true
                obtenerEstadisticas()
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar estadística: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizarEstadistica(id: Long, dto: EstadisticasJugadorDto) {
        viewModelScope.launch {
            _cargando.value = true
            _guardadoExitoso.value = false
            try {
                repository.actualizarEstadistica(id, dto)
                _mensaje.value = "Estadística actualizada con éxito"
                _guardadoExitoso.value = true
                obtenerEstadisticas()
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar estadística: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminarEstadistica(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repository.eliminarEstadistica(id)
                _mensaje.value = "Estadística eliminada con éxito"
                obtenerEstadisticas()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar estadística: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun obtenerTotalGolesEquipo(idEquipo: Int) {
        viewModelScope.launch {
            try {
                _totalGolesEquipo.value = repository.totalGolesEquipo(idEquipo)
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener total goles: ${e.message}"
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