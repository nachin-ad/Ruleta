package com.example.laruletadelasuerte

import androidx.lifecycle.ViewModel

class PanelViewModel : ViewModel() {
    val letrasDesactivadas: MutableSet<Char> = mutableSetOf()
}