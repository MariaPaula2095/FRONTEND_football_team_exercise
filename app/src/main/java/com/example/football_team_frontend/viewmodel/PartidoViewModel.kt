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

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

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
            _guardadoExitoso.value = false
            _mensaje.value = null
            try {
                repository.guardar(partido)
                _mensaje.value = "¡Partido guardado con éxito!"
                obtenerResultados()
                listar()
                _guardadoExitoso.value = true
            } catch (e: Exception) {
                val msg = e.message ?: ""
                _mensaje.value = when {
                    msg.contains("400") -> "No se pudo guardar: Asegúrate de que los equipos sean distintos y los campos estén llenos."
                    msg.contains("500") -> "Error del servidor: No se pudo registrar el partido en este momento."
                    else -> "Error de conexión: Verifica tu internet e inténtalo de nuevo."
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizar(id: Long, partido: Partido) {
        viewModelScope.launch {
            _cargando.value = true
            _guardadoExitoso.value = false
            _mensaje.value = null
            try {
                // Aseguramos que el objeto lleve el ID correcto para evitar errores 400 por desincronización
                val partidoActualizado = partido.copy(idPartido = id)
                repository.actualizar(id, partidoActualizado)
                _mensaje.value = "¡Cambios guardados correctamente!"
                obtenerResultados()
                listar()
                _guardadoExitoso.value = true
            } catch (e: Exception) {
                val msg = e.message ?: ""
                _mensaje.value = when {
                    msg.contains("400") -> "No se pudo actualizar: Revisa que los goles sean válidos y que no haya conflictos en los datos."
                    msg.contains("404") -> "El partido ya no existe o fue eliminado."
                    msg.contains("500") -> "Error del servidor: No se pudo procesar la actualización."
                    else -> "No se pudo conectar: Revisa tu conexión a internet."
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            _guardadoExitoso.value = false
            _mensaje.value = null
            try {
                repository.eliminar(id)
                _mensaje.value = "¡Partido eliminado con éxito!"
                obtenerResultados()
                listar()
                _guardadoExitoso.value = true
            } catch (e: Exception) {
                _mensaje.value = "No se pudo eliminar el partido. Inténtalo más tarde."
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

    fun resetGuardado() {
        _guardadoExitoso.value = false
    }
}