package com.example.laruletadelasuerte

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class VocalesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_vocales, container, false)

        var viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]

        val botones = listOf(
            view.findViewById<Button>(R.id.buttonA),
            view.findViewById<Button>(R.id.buttonE),
            view.findViewById<Button>(R.id.buttonI),
            view.findViewById<Button>(R.id.buttonO),
            view.findViewById<Button>(R.id.buttonU)
        )


        botones.forEach { button ->
            val letra = button.text[0]

            // Desactivar el botón si ya está en la lista de desactivados
            if (viewModel.letrasDesactivadas.contains(letra)) {
                button.isEnabled = false
                button.setBackgroundColor(Color.parseColor("#F0F0F0FF"))
            }

            button.setOnClickListener {
                val letra = button.text[0]
                (activity as? PanelActivity)?.resaltarYRevelarLetra(letra)
                button.isEnabled = false
                button.setBackgroundColor(Color.parseColor("#F0F0F0FF"))
            }
        }

        return view
    }

}