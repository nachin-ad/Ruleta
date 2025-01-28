package com.example.laruletadelasuerte

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class PersonajesActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var btnContinuar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var nombreJugador: TextView
    private var jugadores = mutableListOf<Jugador>()
    private var currentPlayer = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personajes)

        edtNombre = findViewById(R.id.edtNombre)
        btnContinuar = findViewById(R.id.btnContinuar)
        recyclerView = findViewById(R.id.recyclerView)
        nombreJugador = findViewById(R.id.nombreJugador)
        edtNombre.hint = "Jugador ${currentPlayer + 1}: Ingresa tu nombre"

        // Lista de personajes disponibles
        val personajes = listOf(
            R.drawable.personaje1, R.drawable.personaje2, R.drawable.personaje3, R.drawable.personaje4,
            R.drawable.personaje5, R.drawable.personaje6, R.drawable.personaje7, R.drawable.personaje8,
            R.drawable.personaje9, R.drawable.personaje10, R.drawable.personaje11, R.drawable.personaje12
        )

        // Continuar botón
        btnContinuar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            if (nombre.isNotEmpty()) {
                // Agregar jugador con el nombre ingresado y una imagen por defecto
                jugadores.add(Jugador(nombre, 0, 0, R.drawable.personaje1))

                // Ocultar el EditText y el botón hasta que se seleccione el personaje
                edtNombre.visibility = View.GONE
                btnContinuar.visibility = View.GONE
                nombreJugador.visibility = View.GONE

                // Mostrar BottomSheet para la selección del personaje
                showPersonajeBottomSheet(personajes)

            } else {
                edtNombre.error = "Por favor ingresa un nombre"
            }
        }
    }

    private fun showPersonajeBottomSheet(personajes: List<Int>) {
        // Crear y mostrar el BottomSheet con los personajes
        val bottomSheetFragment = SeleccionarPersonajeFragment(personajes, jugadores[currentPlayer].nombre) { imagenResId ->
            // Asignar la imagen seleccionada al jugador actual
            if (currentPlayer in jugadores.indices) {
                jugadores[currentPlayer].imagenResId = imagenResId
            }

            // Actualizar la vista
            recyclerView.visibility = View.GONE
            nombreJugador.visibility = View.VISIBLE

            // Mostrar el nombre de cada jugador y el personaje elegido
            nombreJugador.text = "Jugador ${currentPlayer + 1}: ${jugadores[currentPlayer].nombre} - Personaje seleccionado"

            currentPlayer++

            // Preparar para el siguiente jugador
            if (currentPlayer < 3) {
                edtNombre.visibility = View.VISIBLE
                btnContinuar.visibility = View.VISIBLE
                edtNombre.setText("")
                edtNombre.hint = "Jugador ${currentPlayer + 1}: Ingresa tu nombre"
            } else {
                // Cuando ya se hayan registrado los tres jugadores
                val intent = Intent(this, PanelActivity::class.java)
                intent.putParcelableArrayListExtra("jugadores", ArrayList(jugadores))
                startActivity(intent)
            }
        }

        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

}