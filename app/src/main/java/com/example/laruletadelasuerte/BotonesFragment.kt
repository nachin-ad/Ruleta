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
        // Inflar el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_botones, container, false)
        viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]

        // Encontrar las vistas con findViewById
        val btnVocales = view.findViewById<Button>(R.id.btnVocales)
        val btnRuleta = view.findViewById<Button>(R.id.btnRuleta)
        val btnResolver = view.findViewById<Button>(R.id.btnResolver)

        viewModel.dineroJugadorActual.observe(viewLifecycleOwner) { dineroActual ->
            btnVocales.isClickable = dineroActual >= 50 // Habilita o deshabilita el botón según el dinero
            btnVocales.alpha = if (dineroActual >= 50) 1.0f else 0.8f // Cambia la transparencia del botón
        }

        // Configurar los listeners de los botones
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

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}