package com.example.laruletadelasuerte

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

class ConsonantesFragment : Fragment() {

    private lateinit var viewModel: PanelViewModel
    private var consonantesElegidas = mutableListOf<Char>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflamos el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_consonantes, container, false)


        // Obtenemos el ViewModel compartido
        viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]

        // Lista con los botones
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

        // Observamos el LiveData de letras desactivadas y actualizar los botones
        viewModel.letrasDesactivadasLiveData.observe(viewLifecycleOwner) { letrasDesactivadas ->
            botones.forEach { button ->
                val letra = button.text[0]
                if (letrasDesactivadas.contains(letra)) {
                    button.isEnabled = false
                    button.setBackgroundColor(Color.parseColor("#F0F0F0")) // Gris oscuro
                } else {
                    button.isEnabled = true
                    button.setBackgroundColor(Color.parseColor("#FFFFFF")) // Color original
                }
            }
        }

        // Configurar la acción de los botones
        botones.forEach { button ->
            button.setOnClickListener {
                val letra = button.text[0]

                if (viewModel.ronda == 5) {
                    if (consonantesElegidas.size < 3) {
                        consonantesElegidas.add(letra)
                        (activity as? PanelFinalActivity)?.resaltarYRevelarLetra(letra)
                    }
                    if (consonantesElegidas.size == 3) {
                        (activity as? PanelFinalActivity)?.mostrarVocalesFragment()
                    }
                } else {
                    (activity as? PanelActivity)?.resaltarYRevelarLetra(letra)
                }

                // Agregar la letra desactivada al ViewModel
                viewModel.agregarLetraDesactivada(letra)
            }
        }

        return view
    }
}