package com.example.laruletadelasuerte

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class PanelActivity : AppCompatActivity() {

    lateinit var frase: String
    private lateinit var panel: GridLayout
    private lateinit var letrasVisibles: MutableList<Boolean>
    private lateinit var viewModel: PanelViewModel
    private var jugadorActual = 0
    private lateinit var nombreJugadorActual: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        viewModel = ViewModelProvider(this)[PanelViewModel::class.java]

        viewModel.jugadores = listOf(
            Jugador("Daniel", 0, 0, R.drawable.personaje4),
            Jugador("María", 0, 0, R.drawable.personaje8),
            Jugador("Luis", 0, 0, R.drawable.personaje3)
        )

        nombreJugadorActual = findViewById(R.id.nombreJugadorActual)
        nombreJugadorActual.text = viewModel.jugadores?.get(jugadorActual)?.nombre
        mostrarJugadores()

        panel = findViewById(R.id.letterPanel)

        avanzarRonda()

        setupPanel(panel)

        mostrarBotonesFragment()
    }

    private fun setupPanel(panel: GridLayout) {
        panel.removeAllViews() // Limpia el panel antes de agregar nuevas celdas
        panel.columnCount = 14 // Configura 14 columnas
        panel.rowCount = 4     // Configura 4 filas

        val totalCells = 14 * 4
        var letterIndex = 0

        for (i in 0 until totalCells) {
            if (letterIndex < frase.length) {
                val isVisible = letrasVisibles[letterIndex]
                val char = frase[letterIndex]
                if (char == ' ') {
                    addGrayCell(panel)
                } else if (isVisible) {
                    addCell(panel, char.toString(), i) // Pasa el índice al llamar a addCell
                } else {
                    addCell(panel, "", i) // Celda vacía para letras no reveladas
                }
                letterIndex++
            } else {
                addGrayCell(panel)
            }
        }
    }

    private fun addCell(panel: GridLayout, text: String, index: Int) {
        val textView = TextView(this).apply {
            this.text = text
            textSize = 18f
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.rounded_background_white)
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            layoutParams = GridLayout.LayoutParams().apply {
                width = 65
                height = 80
                setMargins(3, 3, 3, 3)
            }
            tag = index // Almacena el índice para facilitar la identificación
        }
        panel.addView(textView)
    }

    private fun addGrayCell(panel: GridLayout) {
        val grayView = TextView(this).apply {
            text = ""
            setBackgroundResource(R.drawable.rounded_background_gray)
            textSize = 18f
            gravity = Gravity.CENTER
            layoutParams = GridLayout.LayoutParams().apply {
                width = 65
                height = 80
                setMargins(3, 3, 3, 3)
            }
        }
        panel.addView(grayView)
    }

    fun resaltarYRevelarLetra(letra: Char) {
        var count = 0

        // Recorremos las posiciones de la frase
        for (i in frase.indices) {
            if (frase[i].equals(letra, ignoreCase = true)) {
                count++

                // Encuentra la celda correspondiente
                val textView = panel.getChildAt(i) as TextView

                // Cambia el fondo a naranja
                textView.setBackgroundResource(R.drawable.rounded_background_orange)

                // Retrasa la revelación de la letra con fondo blanco
                Handler(Looper.getMainLooper()).postDelayed({
                    letrasVisibles[i] = true
                    textView.text = letra.toString()
                    textView.setBackgroundResource(R.drawable.rounded_background_white)
                }, 1000) // Retraso de 1 segundo
            }
        }

        // Después de todas las iteraciones, actualizamos el mensaje
        val mensaje: String
        if (count > 0) {
            mensaje = "La letra '$letra' aparece $count ${if (count > 1) "veces" else "vez"}"
            calcularDinero(count)
        } else {
            mensaje = "La letra '$letra' no está en la frase"
            actualizarJugador()
        }
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // Verifica si la frase está completamente revelada
        val jugadorActual = viewModel.jugadores?.get(viewModel.jugadorActual) ?: return
        if (frase.indices.all { letrasVisibles[it] || frase[it] == ' ' }) {
            Toast.makeText(this, "Felicidades!! " + jugadorActual.nombre + " ha adivinado la frase", Toast.LENGTH_LONG).show()

            jugadorActual.dineroTotal += jugadorActual.dineroActual

            Handler(Looper.getMainLooper()).postDelayed({
                avanzarRonda()
                mostrarBotonesFragment()
            }, 1500)
        } else{
            // Añade la letra a las desactivadas
            viewModel.letrasDesactivadas.add(letra)

            // Retraso antes de cambiar de fragmento
            Handler(Looper.getMainLooper()).postDelayed({
                mostrarBotonesFragment()
            }, 1200)
        }
    }

    fun calcularDinero(count: Int) {
        val jugadorActual = viewModel.jugadores?.get(viewModel.jugadorActual) ?: return

        when (viewModel.cantidadRuleta) {
            "Pierde turno", "Vocales" -> {
                actualizarJugador()
            }
            "Quiebra" -> {
                jugadorActual.dineroActual = 0
                actualizarJugador()
            }
            "1/2" -> {
                if (jugadorActual.dineroActual > 0) {
                    jugadorActual.dineroActual /= 2
                }
            }
            "x2" -> {
                jugadorActual.dineroActual *= 2
            }
            else -> {
                // Se asume que `cantidadRuleta` se puede convertir a un número
                val cantidad = viewModel.cantidadRuleta.toIntOrNull() ?: 0
                jugadorActual.dineroActual += count * cantidad
            }
        }

        mostrarJugadores()
    }

    fun revelarFraseCompleta() {
        for (i in frase.indices) {
            if (frase[i] != ' ') {
                letrasVisibles[i] = true
            }
        }
        setupPanel(panel) // Actualiza el panel para mostrar toda la frase

    }

    fun mostrarBotonesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, BotonesFragment())
            .commit()
    }

    private fun avanzarRonda() {

        val frases = listOf(
            "  TARTA DE      FRESAS Y       NATA CON     CHOCOLATE",
            "HELADO DE VAINILLA Y CARAMELO",
            "PIZZA DE QUESO Y PEPPERONI"
        )

        frase = frases[viewModel.ronda]
        viewModel.ronda += 1

        letrasVisibles = MutableList(frase.length) { frase[it] == ' ' }

        setupPanel(panel)

        // Notifica a los jugadores del cambio de ronda
        Toast.makeText(this, "¡Nueva frase para la ronda" + viewModel.ronda + "!", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarJugador() {

        viewModel.jugadorActual = (viewModel.jugadorActual + 1) % viewModel.jugadores!!.size
        nombreJugadorActual.text = viewModel.jugadores?.get(viewModel.jugadorActual)?.nombre ?: "Sin nombre"
        mostrarJugadores()
    }

    private fun mostrarJugadores() {

        // Actualiza las vistas con los datos de los jugadores
        val nombres = listOf(
            findViewById<TextView>(R.id.nombreJugador1),
            findViewById(R.id.nombreJugador2),
            findViewById(R.id.nombreJugador3)
        )

        val imagenes = listOf(
            findViewById<ImageView>(R.id.ivJugador1),
            findViewById(R.id.ivJugador2),
            findViewById(R.id.ivJugador3)
        )

        val dinerosActuales = listOf(
            findViewById<TextView>(R.id.dineroActualJugador1),
            findViewById(R.id.dineroActualJugador2),
            findViewById(R.id.dineroActualJugador3)
        )

        val dinerosTotales = listOf(
            findViewById<TextView>(R.id.dineroTotalJugador1),
            findViewById(R.id.dineroTotalJugador2),
            findViewById(R.id.dineroTotalJugador3)
        )

        viewModel.jugadores?.forEachIndexed { index, jugador ->
            nombres[index].text = jugador.nombre
            imagenes[index].setImageResource(jugador.imagenResId)
            dinerosActuales[index].text = jugador.dineroActual.toString()
            dinerosTotales[index].text = jugador.dineroTotal.toString()
        }

    }
}