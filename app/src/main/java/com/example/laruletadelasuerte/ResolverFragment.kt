package com.example.laruletadelasuerte

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ResolverFragment : Fragment() {
    private lateinit var editTextRespuesta: EditText
    private lateinit var btnComprobar: Button
    private lateinit var viewModel: PanelViewModel
    private lateinit var sonidoFallo: MediaPlayer
    private lateinit var sonidoGanar: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño para este fragmento
        val view = inflater.inflate(R.layout.fragment_resolver, container, false)
        viewModel = ViewModelProvider(requireActivity())[PanelViewModel::class.java]


        sonidoFallo = MediaPlayer.create(requireContext(), R.raw.sonidofallo)
        sonidoGanar = MediaPlayer.create(requireContext(), R.raw.sonidoganar)
        editTextRespuesta = view.findViewById(R.id.editTextRespuesta)
        btnComprobar = view.findViewById(R.id.btnComprobar)

        if(viewModel.ronda == 5){
            (activity as? PanelFinalActivity)?.iniciarCuentaRegresiva()
        }

        // Configurar el botón para comprobar la respuesta
        btnComprobar.setOnClickListener {
            if (viewModel.ronda == 5){
                comprobarPanelFinal()
            } else {
                comprobarRespuesta()
            }

        }

        return view
    }

    private fun comprobarRespuesta() {
        val jugadorActual = viewModel.jugadores?.get(viewModel.jugadorActual) ?: return
        val respuestaUsuario = editTextRespuesta.text.toString().replace(" ", "")

        // Obtener la frase correcta desde la actividad
        val fraseCorrecta = (activity as? PanelActivity)?.frase?.replace(" ", "") ?: ""

        if (respuestaUsuario.equals(fraseCorrecta, ignoreCase = true)) {
            Toast.makeText(requireContext(), "¡Correcto! " + jugadorActual.nombre + " ha adivinado la frase", Toast.LENGTH_SHORT).show()
            sonidoGanar.start()

            // Revelar la frase completa en el panel
            (activity as? PanelActivity)?.revelarFraseCompleta()

        } else {
            Toast.makeText(requireContext(), "¡Respuesta incorrecta!", Toast.LENGTH_SHORT).show()
            sonidoFallo.start()

            (activity as? PanelActivity)?.actualizarJugador()
            (activity as? PanelActivity)?.mostrarBotonesFragment()
        }
    }

    private fun comprobarPanelFinal(){
        val respuestaUsuario = editTextRespuesta.text.toString().replace(" ", "")

        // Obtener la frase correcta desde la actividad
        val fraseCorrecta = (activity as? PanelFinalActivity)?.frase?.replace(" ", "") ?: ""

        if (respuestaUsuario.equals(fraseCorrecta, ignoreCase = true)) {
            sonidoGanar.start()

            // Revelar la frase completa en el panel
            (activity as? PanelFinalActivity)?.revelarFraseCompleta()
            (activity as? PanelFinalActivity)?.pararContador()
            mostrarDialogoVictoria()

        } else {
            sonidoFallo.start()
        }
    }

    private fun mostrarDialogoVictoria() {
        AlertDialog.Builder(requireContext())
            .setTitle("¡Felicidades! 🎉")
            .setMessage("Has resuelto la frase. ¡Eres un ganador!")
            .setPositiveButton("Aceptar") { _, _ ->
                // Crear intent para ir a MainActivity
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

                // Cerrar la actividad actual
                (activity as? PanelFinalActivity)?.finish()
            }
            .setCancelable(false)
            .show()
    }

}