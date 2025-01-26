package com.example.laruletadelasuerte

import androidx.lifecycle.ViewModel

class PanelViewModel : ViewModel() {
    var jugadores: List<Jugador>? = null
    var cantidadRuleta: String = ""
    var jugadorActual: Int = 0
    val letrasDesactivadas: MutableSet<Char> = mutableSetOf()
}