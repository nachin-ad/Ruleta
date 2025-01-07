package com.example.ruletadelafortuna

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonJugar: Button = findViewById(R.id.boton_jugar)
        val botonOpciones: Button = findViewById(R.id.boton_opciones)
        val botonReglas: Button = findViewById(R.id.boton_reglas)

        botonJugar.setOnClickListener {
            // Aquí puedes iniciar la actividad de juego
            // startActivity(Intent(this, JuegoActivity::class.java))
        }

        botonOpciones.setOnClickListener {
            // Aquí puedes iniciar la actividad de opciones
            // startActivity(Intent(this, OpcionesActivity::class.java))
        }

        botonReglas.setOnClickListener {
            // Aquí puedes iniciar la actividad de reglas
            // startActivity(Intent(this, ReglasActivity::class.java))
        }
    }
}
