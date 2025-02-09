package com.example.laruletadelasuerte
//clase para guardar partidas
data class Partida(
    val id: Int,
    val nombreJugador1: String,
    val dineroJugador1: Int,
    val nombreJugador2: String,
    val dineroJugador2: Int,
    val nombreJugador3: String,
    val dineroJugador3: Int,
    val ganador: String,
    val fecha: String
)
