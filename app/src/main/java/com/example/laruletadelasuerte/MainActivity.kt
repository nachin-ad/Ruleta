package com.example.laruletadelasuerte

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// Clase principal de la actividad principal del juego
class MainActivity : AppCompatActivity() {
    // Declaración de SharedPreferences para almacenar datos persistentes
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)// Establece el layout de la actividad
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        // Reinicia la preferencia de si se han leído las reglas (se establece en false)
        reiniciarPreferencia()
        // Ajusta los márgenes de la vista principal para adaptarse a la barra de estado y navegación
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //inicializamos todas las variables
        val historial = Historial(this)
        val btnJugar = findViewById<Button>(R.id.boton_jugar)
        val btnHistorial = findViewById<Button>(R.id.boton_historial)
        val btnReglas = findViewById<Button>(R.id.boton_reglas)
        // Configuración del botón "Jugar"
        btnJugar.setOnClickListener {
            if (yaLeidoReglas()) {
                // Si el usuario ya ha leído las reglas, pasa a la selección de personajes
                val intentPanel = Intent(this, PersonajesActivity::class.java)
                startActivity(intentPanel)
            }else{
                // Si no ha leído las reglas, muestra un diálogo preguntando si quiere leerlas
                mostrarDialogo()
            }
        }
        // Configuración del botón "Historial"
        btnHistorial.setOnClickListener{
            if (historial.isHistorialVacio()){
                // Si no hay partidas en el historial, muestra un mensaje informativo
                val alertDialog1 = AlertDialog.Builder(this)
                    .setTitle("Historial vacío")
                    .setMessage("Todavía no tienes partidas guardadas en el historial. ¡Juega algunas partidas primero!")
                    .setPositiveButton("OK"){dialog, _-> dialog.dismiss() }
                    .create()
                alertDialog1.show()
                // Cambia el color del botón del diálogo a azul oscuro
                alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.azul_oscuro))
            }else{
                // Si hay partidas en el historial, abre la actividad del historial
                val intent = Intent(this, HistorialActivity::class.java)
                startActivity(intent)
            }
        }
        btnReglas.setOnClickListener{
            val intent = Intent(this, ReglasActivity::class.java)
            startActivity(intent)
        }
    }

    // Muestra un cuadro de diálogo preguntando si el usuario quiere leer las reglas antes de jugar
    private fun mostrarDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Queréis leer las reglas antes de jugar?")
        builder.setMessage("Puede que sea necesario conocer las reglas antes de comenzar para entender mejor el juego.")
        // Si el usuario elige "Sí", se marca como que ha leído las reglas y se abre la actividad de reglas
        builder.setPositiveButton("Sí") { _, _ ->
            setYaLeidoLasReglas(true)
            val intent = Intent(this, ReglasActivity::class.java)
            startActivity(intent)
        }
        // Si el usuario elige "No", se pasa directamente a la selección de personajes
        builder.setNegativeButton("No") { _, _ ->
            val intentPanel = Intent(this, PersonajesActivity::class.java)
            startActivity(intentPanel)
        }
        builder.setCancelable(false)// No permite cerrar el diálogo tocando fuera


        val dialog = builder.create()
        dialog.show()

        // Cambiar el color de los botones a azul oscuro
        val colorAzulOscuro = ContextCompat.getColor(this, R.color.azul_oscuro)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(colorAzulOscuro)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(colorAzulOscuro)
    }

    // Verifica si el usuario ya ha leído las reglas
    private fun yaLeidoReglas(): Boolean{
        return sharedPreferences.getBoolean("haLeidoReglas", false)
    }

    // Restablece la preferencia "haLeidoReglas" a false
    private fun reiniciarPreferencia(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("haLeidoReglas",false)
        editor.apply()
    }

    // Guarda en SharedPreferences si el usuario ha leído las reglas
    private fun setYaLeidoLasReglas(leido: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("haLeidoReglas", leido)
        editor.apply()
    }
}