package com.example.football_team_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.repository.EntrenadorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EntrenadorViewModel : ViewModel() {
    private val repository = EntrenadorRepository()

    private val _entrenadores = MutableStateFlow<List<Entrenador>>(emptyList())
    val entrenadores: StateFlow<List<Entrenador>> = _entrenadores

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    fun listar() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _entrenadores.value = repository.obtenerEntrenadores()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _cargando.value = false
            }
        }
    }
}
