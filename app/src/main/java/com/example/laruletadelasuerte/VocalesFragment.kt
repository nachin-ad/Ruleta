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

    private lateinit var botones: List<Button>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_vocales, container, false)

        var viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]

        botones = listOf(
            view.findViewById(R.id.buttonA),
            view.findViewById(R.id.buttonE),
            view.findViewById(R.id.buttonI),
            view.findViewById(R.id.buttonO),
            view.findViewById(R.id.buttonU)
        )

        fun activarVocales() {
            botones.forEach { boton ->
                boton.isEnabled = true
                boton.setBackgroundColor(Color.parseColor("#FFFFFF")) // Color original
            }
        }

        viewModel.rondaLiveData.observe(viewLifecycleOwner) {
            activarVocales()
        }

        botones.forEach { boton ->
            val letra = boton.text[0]

            // Desactivar el botón si ya está en la lista de desactivados
            if (viewModel.letrasDesactivadas.contains(letra)) {
                boton.isEnabled = false
                boton.setBackgroundColor(Color.parseColor("#F0F0F0FF"))
            } else {
                boton.setOnClickListener {
                    val letra = boton.text[0]
                    if (viewModel.ronda == 5){
                        (activity as? PanelFinalActivity)?.resaltarYRevelarLetra(letra)
                        (activity as? PanelFinalActivity)?.mostrarResolverFragment()
                    } else {
                        (activity as? PanelActivity)?.resaltarYRevelarLetra(letra)
                    }

                    boton.isEnabled = false
                    boton.setBackgroundColor(Color.parseColor("#F0F0F0FF"))
                }
            }
        }
        return view
    }
}