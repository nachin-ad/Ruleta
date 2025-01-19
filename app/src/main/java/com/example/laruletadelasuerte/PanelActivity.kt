package com.example.laruletadelasuerte

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class PanelActivity : AppCompatActivity() {

    lateinit var frase: String
    private lateinit var panel: GridLayout
    private lateinit var letrasVisibles: MutableList<Boolean>
    private lateinit var viewModel: PanelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        panel = findViewById(R.id.letterPanel)
        viewModel = ViewModelProvider(this)[PanelViewModel::class.java]

        // Frase a revelar
        frase = "  TARTA DE      FRESAS Y       NATA CON     CHOCOLATE"
        letrasVisibles = MutableList(frase.length) { frase[it] == ' ' }

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
        val message = if (count > 0) {
            "La letra '$letra' aparece $count vez${if (count > 1) "es" else ""}."
        } else {
            "La letra '$letra' no está en la frase."
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Añade la letra a las desactivadas
        viewModel.letrasDesactivadas.add(letra)

        // Retraso antes de cambiar de fragmento
        Handler(Looper.getMainLooper()).postDelayed({
            mostrarBotonesFragment()
        }, 1200)
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
}