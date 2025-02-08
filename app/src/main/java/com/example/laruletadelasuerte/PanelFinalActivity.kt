package com.example.laruletadelasuerte

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class PanelFinalActivity : AppCompatActivity() {

    private lateinit var frase: String
    private lateinit var panel: GridLayout
    private lateinit var letrasVisibles: MutableList<Boolean>
    private lateinit var viewModel: PanelViewModel
    private lateinit var jugadorFinal: Jugador
    private lateinit var sonidoAcierto: MediaPlayer
    private val vocales = listOf('A', 'E', 'I', 'O', 'U')
    val letrasFinales = listOf('R', 'S', 'F', 'Y', 'O')
    private lateinit var tvTiempo: TextView
    private var cuentaRegresiva: CountDownTimer? = null
    private var tiempoRestante = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel_final)

        viewModel = ViewModelProvider(this)[PanelViewModel::class.java]
        val jugadores = intent.getParcelableArrayListExtra<Jugador>("jugadores")
        viewModel.ronda = 5

        if (!jugadores.isNullOrEmpty()) {
            jugadorFinal = jugadores.maxByOrNull { it.dineroTotal }!!
        } else {
            Toast.makeText(this, "No hay jugadores disponibles", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        tvTiempo = findViewById(R.id.tvTiempo)

        val nombreJugador = findViewById<TextView>(R.id.nombreJugador)
        nombreJugador.text = jugadorFinal.nombre

        val imagenJugador = findViewById<ImageView>(R.id.ivJugador)
        imagenJugador.setImageResource(jugadorFinal.imagenResId)

        val dineroJugador = findViewById<TextView>(R.id.dineroTotalJugador)
        dineroJugador.text = jugadorFinal.dineroTotal.toString()

        sonidoAcierto = MediaPlayer.create(this, R.raw.sonidoacierto)

        val tvPista = findViewById<TextView>(R.id.tvPista2)
        val frasesPanelFinal = FrasesPanel().frasesPanelFinal
        val (fraseSeleccionada, pista) = frasesPanelFinal.random()

        frase = fraseSeleccionada

        tvPista.text = pista

        letrasVisibles = MutableList(frase.length) { frase[it] == ' ' }
        panel = findViewById(R.id.letterPanel)
        setupPanel(panel)

        mostrarRuletaFragment()


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

        } else {
            mensaje = "La letra '$letra' no está en la frase"
        }
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

        // Añadimos la letra a las desactivadas
        viewModel.letrasDesactivadas.add(letra)

    }

    fun iniciarCuentaRegresiva() {
        tvTiempo?.let {
            cuentaRegresiva = object : CountDownTimer((tiempoRestante * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tiempoRestante = (millisUntilFinished / 1000).toInt()
                    it.text = "$tiempoRestante"
                }

                override fun onFinish() {
                    mostrarDialogoDerrota()
                }
            }.start()
        }
    }

    fun pararContador(){
        cuentaRegresiva?.cancel()
    }

    private fun mostrarDialogoDerrota() {
        AlertDialog.Builder(this) // Ahora usamos "this" porque estamos en una Activity
            .setTitle("¡Tiempo agotado! ⏳")
            .setMessage("No lograste resolver la frase a tiempo. ¡Mejor suerte la próxima vez!")
            .setPositiveButton("Aceptar") { _, _ -> finish() } // Cierra la Activity
            .setCancelable(false)
            .show()
    }

    fun revelarFraseCompleta() {
        for (i in frase.indices) {
            if (frase[i] != ' ') {
                letrasVisibles[i] = true
            }
        }
        setupPanel(panel)
    }

    fun resolverLetras() {
        Handler(Looper.getMainLooper()).postDelayed({
            for (letra in letrasFinales){
                resaltarYRevelarLetra(letra)
            }
        }, 1200)
    }

    fun mostrarRuletaFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, RuletaFragment())
            .commit()
    }

    fun mostrarVocalesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, VocalesFragment())
            .commit()
    }

    fun mostrarResolverFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, ResolverFragment())
            .commit()
    }
}