package com.example.laruletadelasuerte

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class BotonesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_botones, container, false)

        // Encontrar las vistas con findViewById
        val btnVocales = view.findViewById<Button>(R.id.btnVocales)
        val btnRuleta = view.findViewById<Button>(R.id.btnRuleta)
        val btnResolver = view.findViewById<Button>(R.id.btnResolver)

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