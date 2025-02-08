package com.example.laruletadelasuerte

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PanelViewModel : ViewModel() {
    var jugadores: List<Jugador>? = null
    var cantidadRuleta: String = ""
    var jugadorActual: Int = 0
    private val _letrasDesactivadasLiveData = MutableLiveData<MutableSet<Char>>(mutableSetOf())
    val letrasDesactivadasLiveData: LiveData<MutableSet<Char>> get() = _letrasDesactivadasLiveData
    var ronda: Int = 0
    val dineroJugadorActual = MutableLiveData<Int>()
    var bote: Int = 0
    val rondaLiveData = MutableLiveData<Int>()

    fun agregarLetraDesactivada(letra: Char) {
        val letrasActuales = _letrasDesactivadasLiveData.value ?: mutableSetOf()
        letrasActuales.add(letra)
        _letrasDesactivadasLiveData.value = letrasActuales
    }

    // Método para limpiar las letras desactivadas
    fun limpiarLetrasDesactivadas() {
        _letrasDesactivadasLiveData.value = mutableSetOf() // Reinicia el conjunto
    }

    // Actualiza el dinero del jugador actual en el LiveData
    fun actualizarDineroJugadorActual() {
        dineroJugadorActual.value = jugadores?.get(jugadorActual)?.dineroActual
    }
}