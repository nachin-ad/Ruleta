package com.example.laruletadelasuerte

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        reiniciarPreferencia()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnJugar = findViewById<Button>(R.id.boton_jugar)
        val btnHistorial = findViewById<Button>(R.id.boton_opciones)
        val btnReglas = findViewById<Button>(R.id.boton_reglas)

        btnJugar.setOnClickListener {
            if (yaLeidoReglas()) {
                val intentPanel = Intent(this, PersonajesActivity::class.java)
                startActivity(intentPanel)
            }else{
                mostrarDialogo()
            }
        }
        btnHistorial.setOnClickListener{
            val intent = Intent(this, Historial::class.java)
            startActivity(intent)
        }
        btnReglas.setOnClickListener{
            val intent = Intent(this, ReglasActivity::class.java)
            startActivity(intent)
        }
    }
    private fun mostrarDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Queréis leer las reglas antes de jugar?")
        builder.setMessage("Puede que sea necesario leer las reglas antes de comenzar para entender mejor el juego.")
        builder.setPositiveButton("Sí") { _, _ ->
            setYaLeidoLasReglas(true)
            val intent = Intent(this, ReglasActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton("No") { _, _ ->
            val intentPanel = Intent(this, PersonajesActivity::class.java)
            startActivity(intentPanel)
        }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }
    private fun yaLeidoReglas(): Boolean{
        return sharedPreferences.getBoolean("haLeidoReglas", false)
    }
    private fun reiniciarPreferencia(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("haLeidoReglas",false)
        editor.apply()
    }
    private fun setYaLeidoLasReglas(leido: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("haLeidoReglas", leido)
        editor.apply()
    }
}