package com.example.football_team_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EquipoViewModel : ViewModel() {

    private val repository = EquipoRepository()

    // Lista ORIGINAL (sin filtro)
    private var listaOriginal: List<Equipo> = emptyList()

    // Lista que ve la UI (filtrada)
    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Texto de búsqueda
    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    init {
        cargarEquipos()
    }

    // =============================
    // CARGAR EQUIPOS
    // =============================
    fun cargarEquipos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                listaOriginal = repository.obtenerEquipos()
                _equipos.value = listaOriginal
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar equipos"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // =============================
    // BUSCAR (FILTRO LOCAL)
    // =============================
    fun onSearchChange(texto: String) {
        _search.value = texto

        _equipos.value = if (texto.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                it.nombre.contains(texto, ignoreCase = true) ||
                        it.ciudad.contains(texto, ignoreCase = true)
            }
        }
    }

    // =============================
    // CREAR EQUIPO
    // =============================
    fun guardarEquipo(equipo: Equipo) {
        viewModelScope.launch {
            try {
                repository.guardarEquipo(equipo)
                cargarEquipos() // recargar lista
            } catch (e: Exception) {
                _error.value = "Error al guardar equipo"
            }
        }
    }

    // =============================
    // ELIMINAR EQUIPO
    // =============================
    fun eliminarEquipo(id: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarEquipo(id)
                cargarEquipos() // recargar lista
            } catch (e: Exception) {
                _error.value = "Error al eliminar equipo"
            }
        }
    }

    // =============================
    // ACTUALIZAR EQUIPO
    // =============================
    fun actualizarEquipo(id: Long, equipo: Equipo) {
        viewModelScope.launch {
            try {
                repository.actualizarEquipo(id, equipo)
                cargarEquipos()
            } catch (e: Exception) {
                _error.value = "Error al actualizar equipo"
            }
        }
    }
}