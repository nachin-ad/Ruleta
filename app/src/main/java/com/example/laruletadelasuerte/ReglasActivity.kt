package com.example.laruletadelasuerte

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReglasActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reglas)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        val tvReglas: TextView = findViewById(R.id.tvReglas)
        val btnVolver: Button = findViewById(R.id.btnVolver)

        val textoTitulo = """
            Normas de la Ruleta de
            📜                la Suerte               🎡
            """.trimIndent()

        val textoReglas = """
            
            1️⃣ Participantes:
            
            El juego cuenta con tres concursantes que compiten entre sí para acumular la mayor cantidad de puntos.  
            
            2️⃣ La Ruleta:
            
            La ruleta está dividida en secciones que incluyen premios en efectivo, viajes y casillas especiales como "Quiebra" o "Pierde un Turno".  
            
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
            
            El concursante con más puntos avanza a la **gran final**, donde tendrá la oportunidad de ganar el premio mayor.  
            
            ¡Buena suerte y a girar la ruleta!
        """.trimIndent()

        tvTitulo.text = textoTitulo
        tvReglas.text = textoReglas

        btnVolver.setOnClickListener{
            finish()
        }

    }

    override fun onPause(){
        super.onPause()
        setYaLeidoLasReglas(true)
    }
    private fun setYaLeidoLasReglas(leido: Boolean){
     val editor = sharedPreferences.edit()
     editor.putBoolean("haLeidoReglas",leido)
     editor.apply()
    }
}