package com.example.laruletadelasuerte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class ResolverFragment : Fragment() {
    private lateinit var editTextRespuesta: EditText
    private lateinit var btnComprobar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_resolver, container, false)

        editTextRespuesta = view.findViewById(R.id.editTextRespuesta)
        btnComprobar = view.findViewById(R.id.btnComprobar)

        // Configurar el botón para comprobar la respuesta
        btnComprobar.setOnClickListener {
            comprobarRespuesta()
        }

        return view
    }

    private fun comprobarRespuesta() {
        val respuestaUsuario = editTextRespuesta.text.toString().replace(" ", "")

        // Obtener la frase correcta desde la actividad
        val fraseCorrecta = (activity as? PanelActivity)?.frase?.replace(" ", "") ?: ""

        if (respuestaUsuario.equals(fraseCorrecta, ignoreCase = true)) {
            Toast.makeText(requireContext(), "¡Correcto! Has resuelto la frase.", Toast.LENGTH_SHORT).show()

            // Revelar la frase completa en el panel
            (activity as? PanelActivity)?.revelarFraseCompleta()

            // Opcional: Navegar a otro fragmento o reiniciar el juego
            (activity as? PanelActivity)?.mostrarBotonesFragment()
        } else {
            Toast.makeText(requireContext(), "Respuesta incorrecta. ¡Inténtalo de nuevo!", Toast.LENGTH_SHORT).show()
        }
    }
}