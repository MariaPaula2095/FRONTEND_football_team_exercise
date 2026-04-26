package com.example.football_team_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.repository.EntrenadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EntrenadorViewModel : ViewModel() {

    private val repository = EntrenadorRepository()

    private var listaOriginal: List<Entrenador> = emptyList()

    private val _entrenadores = MutableStateFlow<List<Entrenador>>(emptyList())
    val entrenadores: StateFlow<List<Entrenador>> = _entrenadores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    init {
        cargarEntrenadores()
    }

    // =============================
    // CARGAR
    // =============================
    fun cargarEntrenadores() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                listaOriginal = repository.obtenerEntrenadores()
                _entrenadores.value = listaOriginal
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar entrenadores"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // =============================
    // BUSCAR
    // =============================
    fun onSearchChange(texto: String) {
        _search.value = texto
        _entrenadores.value = if (texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nombre.contains(texto, ignoreCase = true) ||
                        it.especialidad.contains(texto, ignoreCase = true)
            }
        }
    }

    // =============================
    // GUARDAR
    // =============================
    fun guardarEntrenador(entrenador: Entrenador) {
        viewModelScope.launch {
            try {
                repository.guardarEntrenador(entrenador)
                cargarEntrenadores()
            } catch (e: Exception) {
                _error.value = "Error al guardar entrenador"
            }
        }
    }

    // =============================
    // ACTUALIZAR
    // =============================
    fun actualizarEntrenador(id: Long, entrenador: Entrenador) {
        viewModelScope.launch {
            try {
                repository.actualizarEntrenador(id, entrenador)
                cargarEntrenadores()
            } catch (e: Exception) {
                _error.value = "Error al actualizar entrenador"
            }
        }
    }

    // =============================
    // ELIMINAR
    // =============================
    fun eliminarEntrenador(id: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarEntrenador(id)
                cargarEntrenadores()
            } catch (e: Exception) {
                _error.value = "Error al eliminar entrenador"
            }
        }
    }
}