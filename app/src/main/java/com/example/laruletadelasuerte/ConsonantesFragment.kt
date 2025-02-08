package com.example.laruletadelasuerte

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class ConsonantesFragment : Fragment() {

    private lateinit var viewModel: PanelViewModel
    private var consonantesElegidas = mutableListOf<Char>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflar el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_consonantes, container, false)


        // Obtener el ViewModel compartido
        viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]

        val botones = listOf(
            view.findViewById<Button>(R.id.buttonB),
            view.findViewById<Button>(R.id.buttonC),
            view.findViewById<Button>(R.id.buttonD),
            view.findViewById<Button>(R.id.buttonF),
            view.findViewById<Button>(R.id.buttonG),
            view.findViewById<Button>(R.id.buttonH),
            view.findViewById<Button>(R.id.buttonJ),
            view.findViewById<Button>(R.id.buttonK),
            view.findViewById<Button>(R.id.buttonL),
            view.findViewById<Button>(R.id.buttonM),
            view.findViewById<Button>(R.id.buttonN),
            view.findViewById<Button>(R.id.buttonÑ),
            view.findViewById<Button>(R.id.buttonP),
            view.findViewById<Button>(R.id.buttonQ),
            view.findViewById<Button>(R.id.buttonR),
            view.findViewById<Button>(R.id.buttonS),
            view.findViewById<Button>(R.id.buttonT),
            view.findViewById<Button>(R.id.buttonV),
            view.findViewById<Button>(R.id.buttonW),
            view.findViewById<Button>(R.id.buttonX),
            view.findViewById<Button>(R.id.buttonY),
            view.findViewById<Button>(R.id.buttonZ),
        )

        fun actualizarBotones() {
            botones.forEach { button ->
                button.isEnabled = true
                button.setBackgroundColor(Color.parseColor("#FFFFFF")) // Color original
            }
        }

        viewModel.rondaLiveData.observe(viewLifecycleOwner) {
            actualizarBotones()
        }

        botones.forEach { button ->
            val letra = button.text[0]

            // Desactivar el botón si ya está en la lista de desactivados
            if (viewModel.letrasDesactivadas.contains(letra)) {
                button.isEnabled = false
                button.setBackgroundColor(Color.parseColor("#F0F0F0FF"))
            } else {
                button.setOnClickListener {
                    if (viewModel.ronda == 5){
                        if (consonantesElegidas.size < 3){
                            consonantesElegidas.add(letra)
                            (activity as? PanelFinalActivity)?.resaltarYRevelarLetra(letra)
                            button.isEnabled = false
                            button.setBackgroundColor(Color.parseColor("#F0F0F0FF"))

                            if (consonantesElegidas.size == 3) {
                                (activity as? PanelFinalActivity)?.mostrarVocalesFragment()
                            }
                        }
                    } else {
                        (activity as? PanelActivity)?.resaltarYRevelarLetra(letra)
                    }
                    button.isEnabled = false
                    button.setBackgroundColor(Color.parseColor("#F0F0F0FF"))
                }
            }

        }

        return view
    }

}