package com.example.laruletadelasuerte

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class BotonesFragment : Fragment() {

    private lateinit var viewModel: PanelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_botones, container, false)

        // ViewModel para manejar la lógica del panel
        viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]

        // Obtenemos las referencias a los botones
        val btnVocales = view.findViewById<Button>(R.id.btnVocales)
        val btnRuleta = view.findViewById<Button>(R.id.btnRuleta)
        val btnResolver = view.findViewById<Button>(R.id.btnResolver)

        // Observador para actualizar la habilitación del botón de vocales
        viewModel.dineroJugadorActual.observe(viewLifecycleOwner) { dineroActual ->
            btnVocales.isClickable = dineroActual >= 50
            btnVocales.alpha = if (dineroActual >= 50) 1.0f else 0.8f
        }

        // Forzamos la actualización al crear la vista
        viewModel.actualizarDineroJugadorActual()

        // Configuramoss los listeners de los botones
        btnVocales.setOnClickListener {
            replaceFragment(VocalesFragment())
        }

        btnRuleta.setOnClickListener {
            replaceFragment(RuletaFragment())
        }

        btnResolver.setOnClickListener {
            replaceFragment(ResolverFragment())
        }

        return view
    }

    // Método para reemplazar dinámicamente el fragmento dentro del contenedor
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}