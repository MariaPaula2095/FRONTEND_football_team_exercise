package com.example.football_team_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football_team_frontend.model.Partido
import com.example.football_team_frontend.model.ResultadoPartido
import com.example.football_team_frontend.repository.PartidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PartidoViewModel : ViewModel() {

    private val repository = PartidoRepository()

    // ================= ESTADOS =================

    private val _partidos = MutableStateFlow<List<Partido>>(emptyList())
    val partidos: StateFlow<List<Partido>> = _partidos

    private val _resultados = MutableStateFlow<List<ResultadoPartido>>(emptyList())
    val resultados: StateFlow<List<ResultadoPartido>> = _resultados

    private val _totalGoles = MutableStateFlow<Int>(0)
    val totalGoles: StateFlow<Int> = _totalGoles

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    // ================= CRUD =================

    fun listar() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _partidos.value = repository.listar()
            } catch (e: Exception) {
                _mensaje.value = "Error al listar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun guardar(partido: Partido) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repository.guardar(partido)
                _mensaje.value = "Partido guardado correctamente"
                listar()
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizar(id: Long, partido: Partido) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repository.actualizar(id, partido)
                _mensaje.value = "Partido actualizado correctamente"
                listar()
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
                _mensaje.value = "Partido eliminado correctamente"
                listar()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    // ================= CONSULTAS NATIVAS =================

    fun totalGolesPorEquipo(equipoId: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _totalGoles.value = repository.totalGolesPorEquipo(equipoId)
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener goles: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun obtenerResultados() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _resultados.value = repository.obtenerResultados()
            } catch (e: Exception) {
                _mensaje.value = "Error al obtener resultados: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}