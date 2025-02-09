package com.example.laruletadelasuerte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


// Fragmento que muestra un BottomSheet para seleccionar un personaje
class SeleccionarPersonajeFragment(
    private val personajes: List<Int>, // Lista de personajes (identificadores de imágenes)
    private val jugadorActual: String, // Nombre del jugador actual
    private val onItemClick: (Int) -> Unit // Callback cuando se selecciona un personaje
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seleccionar_personaje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los elementos del layout
        val tvJugador = view.findViewById<TextView>(R.id.rvNombre)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        // Configuración del RecyclerView en una cuadrícula de 3 columnas
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // Adaptador que muestra los personajes y gestiona la selección
        recyclerView.adapter = PersonajeAdapter(personajes) { imagenResId ->
            recyclerView.postDelayed({
                onItemClick(imagenResId) // Llama al callback con el personaje seleccionado
                dismiss() // Cierra el BottomSheet después de la selección
            }, 500) // Pequeño retraso para que se vea la animación
        }

        // Mensaje de bienvenida personalizado para el jugador actual
        tvJugador.text = "Hola $jugadorActual \n Elige un personaje:"
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let { sheet ->
                // Ajustar el BottomSheet para que ocupe toda la pantalla
                val layoutParams = sheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                sheet.layoutParams = layoutParams
            }
            // Deshabilitar el arrastre para que solo se cierre cuando se seleccione un personaje
            it.behavior.isDraggable = false
        }
    }
}
