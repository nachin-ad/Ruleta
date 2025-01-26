package com.example.laruletadelasuerte

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PanelViewModel : ViewModel() {
    var jugadores: List<Jugador>? = null
    var cantidadRuleta: String = ""
    var jugadorActual: Int = 0
    val letrasDesactivadas: MutableSet<Char> = mutableSetOf()
    var ronda: Int = 0
    val dineroJugadorActual = MutableLiveData<Int>()

    // Actualiza el dinero del jugador actual en el LiveData
    fun actualizarDineroJugadorActual() {
        dineroJugadorActual.value = jugadores?.get(jugadorActual)?.dineroActual
    }
}