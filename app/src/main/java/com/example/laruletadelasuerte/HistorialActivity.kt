package com.example.laruletadelasuerte


    import android.os.Bundle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView

class HistorialActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_historial)

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistorial)
            val dbHelper = Historial(this)
            val historial = dbHelper.obtenerHistorial()

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistorialAdapter(this, historial)
        }
    }