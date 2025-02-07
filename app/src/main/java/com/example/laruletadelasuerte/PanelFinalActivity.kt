package com.example.laruletadelasuerte

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class PanelFinalActivity : AppCompatActivity() {

    lateinit var frase: String
    private lateinit var panel: GridLayout
    private lateinit var viewModel: PanelViewModel
    private lateinit var nombreJugador: TextView
    private lateinit var dineroJugador: TextView
    private lateinit var personajeJugador: ImageView
    private lateinit var jugadores: List<Jugador>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel_final)

        val bundle = intent.extras
        jugadores = bundle?.getParcelableArrayList("jugadores")!!

        val jugadorConMasDinero = viewModel.jugadores?.maxByOrNull { it.dineroTotal }
        nombreJugador = findViewById(R.id.nombreJugador)
        personajeJugador = findViewById(R.id.ivJugador)
        dineroJugador = findViewById(R.id.dineroTotalJugador)

        if (jugadorConMasDinero != null) {
            nombreJugador.text = jugadorConMasDinero.nombre
            personajeJugador.setImageResource(jugadorConMasDinero.imagenResId)
            dineroJugador.text = jugadorConMasDinero.dineroTotal.toString()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout2, RuletaFragment())
            .commit()

    }


}