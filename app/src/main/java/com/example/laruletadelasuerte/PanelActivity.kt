package com.example.laruletadelasuerte

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class PanelActivity : AppCompatActivity() {

    // Variables de estado
    lateinit var frase: String
    private lateinit var panel: GridLayout
    private lateinit var letrasVisibles: MutableList<Boolean>
    private lateinit var viewModel: PanelViewModel
    private var jugadorActual = 0
    private lateinit var nombreJugadorActual: TextView
    private lateinit var sonidoAcierto: MediaPlayer
    private lateinit var sonidoFallo: MediaPlayer
    private val vocales = listOf('A', 'E', 'I', 'O', 'U')
    private var vocalesAdivinadas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel)

        // Inicialización del viewModel
        viewModel = ViewModelProvider(this)[PanelViewModel::class.java]

        sonidoAcierto = MediaPlayer.create(this, R.raw.sonidoacierto)
        sonidoFallo = MediaPlayer.create(this, R.raw.sonidofallo)
        sonidoFallo.setVolume(0.3f, 0.3f)

        //Lista de jugadores
        val bundle = intent.extras
        viewModel.jugadores = bundle?.getParcelableArrayList("jugadores")


        viewModel.actualizarDineroJugadorActual()
        nombreJugadorActual = findViewById(R.id.nombreJugadorActual)
        nombreJugadorActual.text = viewModel.jugadores?.get(jugadorActual)?.nombre
        mostrarJugadores()

        panel = findViewById(R.id.letterPanel)
        avanzarRonda()
        setupPanel(panel)
        mostrarBotonesFragment()
    }

    // Función para configurar el panel con celdas según la frase
    private fun setupPanel(panel: GridLayout) {
        panel.removeAllViews()
        panel.columnCount = 14
        panel.rowCount = 4

        val totalCells = 14 * 4
        var letterIndex = 0

        // Recorremos cada celda y agregamos un elemento al panel
        for (i in 0 until totalCells) {
            if (letterIndex < frase.length) {
                val isVisible = letrasVisibles[letterIndex]
                val char = frase[letterIndex]
                if (char == ' ') {
                    addGrayCell(panel) // Celda gris para los espacios
                } else if (isVisible) {
                    addCell(panel, char.toString(), i) // Celda visible con la letra
                } else {
                    addCell(panel, "", i) // Celda vacía para letras no reveladas
                }
                letterIndex++
            } else {
                addGrayCell(panel) // Rellenamos las celdas restantes con gris
            }
        }
    }

    // Función para agregar una celda con una letra
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
            tag = index
        }
        panel.addView(textView)
    }

    // Función para agregar una celda gris para espacios vacíos
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

    // Función para revelar una letra en la frase y actualizar las celdas
    fun resaltarYRevelarLetra(letra: Char) {
        var count = 0

        // Recorremos las posiciones de la frase
        for (i in frase.indices) {
            if (frase[i].equals(letra, ignoreCase = true)) {
                count++

                // Obtenemos la celda correspondiente
                val textView = panel.getChildAt(i) as TextView

                textView.setBackgroundResource(R.drawable.rounded_background_orange)

                Handler(Looper.getMainLooper()).postDelayed({
                    letrasVisibles[i] = true
                    textView.text = letra.toString()
                    textView.setBackgroundResource(R.drawable.rounded_background_white)
                }, 1000)
            }
        }

        // Verificamos si la letra es una vocal
        val esVocal = letra.uppercaseChar() in vocales

        // Actualizamos el mensaje
        val mensaje: String
        if (count > 0) {
            mensaje = "La letra '$letra' aparece $count ${if (count > 1) "veces" else "vez"}"
            sonidoAcierto.start()

            if (viewModel.ronda == 4) {
                viewModel.bote += count * 100
                val tvPista = findViewById<TextView>(R.id.tvPista)
                tvPista.text = "Bote: ${viewModel.bote}"
            }

            if (!vocalesAdivinadas) {
                // Restamos 50 al dinero del jugador actual si es una vocal
                if (esVocal) {
                    val jugadorActual = viewModel.jugadores?.get(viewModel.jugadorActual)
                    jugadorActual?.let {
                        it.dineroActual -= 50
                        if (it.dineroActual < 0) it.dineroActual =
                            0 // Evitar que el dinero sea negativo
                        mostrarJugadores() // Actualiza la interfaz con los nuevos valores
                        viewModel.actualizarDineroJugadorActual()
                    }
                } else {
                    calcularDinero(count)
                    viewModel.actualizarDineroJugadorActual()
                }
            } else {
                vocalesAdivinadas = true
            }

        } else {
            mensaje = "La letra '$letra' no está en la frase"
            if (!viewModel.cantidadRuleta.equals("Vocales")) {
                sonidoFallo.start()
            }
            actualizarJugador()
        }
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // Añadimos la letra a las desactivadas
        viewModel.letrasDesactivadas.add(letra)

        Handler(Looper.getMainLooper()).postDelayed({
            mostrarBotonesFragment()
        }, 1200)

    }

    fun calcularDinero(count: Int) {
        val jugadorActual = viewModel.jugadores?.get(viewModel.jugadorActual) ?: return

        when (viewModel.cantidadRuleta) {
            "Pierde turno" -> {
                sonidoFallo.start()
                actualizarJugador()
            }

            "Quiebra" -> {
                sonidoFallo.start()
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

            "Vocales" -> {
                if (!vocalesAdivinadas) {
                    vocalesAdivinadas = true
                    for (letra in vocales) {
                        if (!viewModel.letrasDesactivadas.contains(letra)) {
                            resaltarYRevelarLetra(letra)
                        }
                    }
                }
            }

            "BOTE" -> {
                jugadorActual.dineroActual += viewModel.bote
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
        setupPanel(panel)

        if (viewModel.ronda != 4) {
            avanzarRonda()
        } else {
            val intentPanelFinal = Intent(this, PanelFinalActivity::class.java)
            intentPanelFinal.putParcelableArrayListExtra("jugadores",
                viewModel.jugadores?.let { ArrayList(it) })
            startActivity(intentPanelFinal)
        }

    }

    fun mostrarBotonesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, BotonesFragment())
            .commit()
    }

    private fun avanzarRonda() {
        // Sumamos el dineroActual del jugador actual a su dineroTotal
        val jugadorActual = viewModel.jugadores?.get(viewModel.jugadorActual)
        jugadorActual?.let {
            it.dineroTotal += it.dineroActual
        }

        // Reiniciamos el dineroActual de todos los jugadores a 0
        viewModel.jugadores?.forEach { jugador ->
            jugador.dineroActual = 0
        }

        val frasesPanel = FrasesPanel()
        viewModel.ronda++

        // TextView donde se muestra la pista o el bote
        val tvPista = findViewById<TextView>(R.id.tvPista)

        if (viewModel.ronda == 4) {
            // Configuración específica para la ronda 4
            viewModel.bote = 0 // Reinicia el bote al iniciar la ronda
            frase = frasesPanel.frasesPorRonda[viewModel.ronda]?.random()?.first ?: ""

            tvPista.text = "Bote: ${viewModel.bote}"

        } else {
            // Configuración para otras rondas
            val frasesDeLaRonda = frasesPanel.frasesPorRonda[viewModel.ronda]
            val (fraseSeleccionada, pista) = frasesDeLaRonda!!.random()

            frase = fraseSeleccionada

            tvPista.text = pista
        }

        viewModel.rondaLiveData.value = viewModel.ronda
        viewModel.letrasDesactivadas.clear()
        letrasVisibles = MutableList(frase.length) { frase[it] == ' ' }

        Handler(Looper.getMainLooper()).postDelayed({
            setupPanel(panel)
        }, 1800)


        Toast.makeText(
            this,
            "¡Nueva frase para la ronda" + viewModel.ronda + "!",
            Toast.LENGTH_SHORT
        ).show()

        if (viewModel.ronda != 1) {
            actualizarJugador()
        } else {
            mostrarJugadores()
        }

        mostrarBotonesFragment()
    }

    fun actualizarJugador() {
        viewModel.jugadorActual = (viewModel.jugadorActual + 1) % viewModel.jugadores!!.size
        nombreJugadorActual.text =
            viewModel.jugadores?.get(viewModel.jugadorActual)?.nombre ?: "Sin nombre"
        mostrarJugadores()
    }

    private fun mostrarJugadores() {
        // Actualizamos las vistas con los datos de los jugadores
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

    //guardar el jugador que gane en la base de datos
    /*
    * val dbHistorial = Historial(this)
    val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    dbHistorial.guardarPartida(
    viewModel.jugadores?.get(0)?.nombre ?: "",
    viewModel.jugadores?.get(0)?.dineroTotal ?: 0,
    viewModel.jugadores?.get(1)?.nombre ?: "",
    viewModel.jugadores?.get(1)?.dineroTotal ?: 0,
    viewModel.jugadores?.get(2)?.nombre ?: "",
    viewModel.jugadores?.get(2)?.dineroTotal ?: 0,
    ganador = determinarGanador(), // Función que devuelvería el nombre del ganador
    fecha = fechaActual
)
    * */

}