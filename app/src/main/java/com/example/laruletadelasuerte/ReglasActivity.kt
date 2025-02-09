package com.example.laruletadelasuerte

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ReglasActivity : AppCompatActivity() {

    // SharedPreferences para almacenar si el usuario ha leído las reglas
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el diseño de borde a borde en dispositivos compatibles
        setContentView(R.layout.activity_reglas)

        // Inicializa SharedPreferences con el nombre "MyPrefs"
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Referencias a los elementos de la interfaz
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        val tvReglas: TextView = findViewById(R.id.tvReglas)
        val btnVolver: Button = findViewById(R.id.btnVolver)

        // Texto del título con formato
        val textoTitulo = """
            Normas de la Ruleta de
            📜            la Suerte             🎡
        """.trimIndent()

        // Texto con las reglas del juego
        val textoReglas = """
            
            1️⃣ Participantes:
            
            El juego cuenta con tres concursantes que compiten entre sí para acumular la mayor cantidad de puntos.  
            
            2️⃣ La Ruleta:
            
            La ruleta está dividida en secciones que incluyen puntos y casillas especiales como "Quiebra", "Pierde un Turno" o "Todas las vocales".  
            
            3️⃣ Turnos:
            
            Los concursantes giran la ruleta en su turno y deben esperar a que se detenga para conocer el resultado.  
            
            4️⃣ Selección de Letras:
            
               - Si la ruleta cae en una casilla con valor, el concursante elige una "consonante". Si está en la palabra o frase oculta, se revela y el jugador suma puntos.  
               - También pueden comprar "vocales", siempre que dispongan de saldo suficiente.  
            
            5️⃣ Casilla de Quiebra:
            
            Si la ruleta cae en "Quiebra", el concursante pierde todos los puntos acumulados en la ronda.  
            
            6️⃣ Final de la Ronda:
             
            La ronda termina cuando un concursante adivina correctamente la palabra o frase oculta. Los puntos acumulados se suman a su marcador total.  
            
            7️⃣ Ronda Final:
            
            El concursante con más puntos avanza a la gran final, donde tendrá la oportunidad de ganar el premio mayor.  
            
            ¡Buena suerte y a girar la ruleta!
        """.trimIndent()

        // Asigna los textos a los elementos de la interfaz
        tvTitulo.text = textoTitulo
        tvReglas.text = textoReglas

        // Configura el botón para cerrar la actividad y volver atrás
        btnVolver.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        setYaLeidoLasReglas(true) // Marca que el usuario ha leído las reglas
    }

    // Guarda en SharedPreferences si el usuario ya ha leído las reglas
    private fun setYaLeidoLasReglas(leido: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("haLeidoReglas", leido)
        editor.apply()
    }
}
