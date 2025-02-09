package com.example.laruletadelasuerte


import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialActivity : AppCompatActivity() {
        //clase que maneja el diseño del historial
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_historial)
            //inicializamos las variables de la vista
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistorial)
            val dbHelper = Historial(this)
            val historial = dbHelper.obtenerHistorial()

            //inicializamos y asignamos la funcion finish al boton volver
            val btnVolver = findViewById<Button>(R.id.btnVolver)
            btnVolver.setOnClickListener{
                finish()
            }

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistorialAdapter(this, historial)
        }
    }