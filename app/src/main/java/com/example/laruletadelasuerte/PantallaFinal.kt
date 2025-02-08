package com.example.laruletadelasuerte

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PantallaFinal : AppCompatActivity() {

    lateinit var txtPartidaTerminada: TextView
    lateinit var txtJugador1: TextView
    lateinit var txtDinero1: TextView
    lateinit var txtJugador2: TextView
    lateinit var txtDinero2: TextView
    lateinit var txtJugador3: TextView
    lateinit var txtDinero3: TextView
    lateinit var txtGanador: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_final)

        txtPartidaTerminada = findViewById(R.id.txtPartidaTerminada)
        txtJugador1 = findViewById(R.id.txtJugador1)
        txtDinero1 = findViewById(R.id.txtDinero1)
        txtJugador2 = findViewById(R.id.txtJugador2)
        txtDinero2 = findViewById(R.id.txtDinero2)
        txtJugador3 = findViewById(R.id.txtJugador3)
        txtDinero3 = findViewById(R.id.txtDinero3)
        txtGanador = findViewById(R.id.txtGanador)

        val historial = Historial(this)

        val ultimaPartida = historial.obtenerUltimaPartida()

        if (ultimaPartida != null) {

            txtPartidaTerminada.text = "Partida Terminada"

            txtJugador1.text = ultimaPartida.nombreJugador1
            txtDinero1.text = "${ultimaPartida.dineroJugador1}€"

            txtJugador2.text = ultimaPartida.nombreJugador2
            txtDinero2.text = "${ultimaPartida.dineroJugador2}€"

            txtJugador3.text = ultimaPartida.nombreJugador3
            txtDinero3.text = "${ultimaPartida.dineroJugador3}€"

            txtGanador.text = "¡Ganador: ${ultimaPartida.ganador}!"
        }
    }
}