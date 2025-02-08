package com.example.laruletadelasuerte

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SeleccionarPersonajeFragment(
    private val personajes: List<Int>,
    private val jugadorActual: String,
    private val onItemClick: (Int) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seleccionar_personaje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvjugador = view.findViewById<TextView>(R.id.rvNombre)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        recyclerView.adapter = PersonajeAdapter(personajes) { imagenResId ->
            recyclerView.postDelayed({
                onItemClick(imagenResId) // Llamamos al callback con la imagen seleccionada
                dismiss() // Cerramos el BottomSheet después de seleccionar el personaje
            },500)
        }

        tvjugador.text = "Hola $jugadorActual \n Elige un personaje:"

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val layoutParams = sheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                sheet.layoutParams = layoutParams
            }
            it.behavior.isDraggable = false
        }
    }

}