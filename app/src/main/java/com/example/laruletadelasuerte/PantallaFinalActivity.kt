package com.example.laruletadelasuerte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Clase que representa la pantalla final de la partida
class PantallaFinalActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la UI
    lateinit var txtPartidaTerminada: TextView
    lateinit var txtJugador1: TextView
    lateinit var txtDinero1: TextView
    lateinit var txtJugador2: TextView
    lateinit var txtDinero2: TextView
    lateinit var txtJugador3: TextView
    lateinit var txtDinero3: TextView
    lateinit var txtGanador: TextView
    lateinit var btnVolverAJugar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_final) // Asigna el layout a la actividad

        // Inicialización del botón "Volver a Jugar"
        btnVolverAJugar = findViewById(R.id.btnVolverAJugar)

        // Configura el botón para volver a la pantalla principal
        btnVolverAJugar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza esta actividad para evitar acumulación en la pila
        }

        // Inicialización de los elementos de texto
        txtPartidaTerminada = findViewById(R.id.txtPartidaTerminada)
        txtJugador1 = findViewById(R.id.txtJugador1)
        txtDinero1 = findViewById(R.id.txtDinero1)
        txtJugador2 = findViewById(R.id.txtJugador2)
        txtDinero2 = findViewById(R.id.txtDinero2)
        txtJugador3 = findViewById(R.id.txtJugador3)
        txtDinero3 = findViewById(R.id.txtDinero3)
        txtGanador = findViewById(R.id.txtGanador)

        // Instancia de la clase Historial para obtener datos de la última partida
        val historial = Historial(this)

        // Recupera la última partida guardada en el historial
        val ultimaPartida = historial.obtenerUltimaPartida()

        // Si hay una partida registrada, muestra los datos en la pantalla
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