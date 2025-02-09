package com.example.laruletadelasuerte

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para mostrar una lista de personajes en un RecyclerView
class PersonajeAdapter(
    private val personajes: List<Int>, // Lista de identificadores de recursos de imágenes
    private val onItemClick: (Int) -> Unit // Función lambda que se ejecuta al hacer clic en un personaje
) : RecyclerView.Adapter<PersonajeAdapter.PersonajeViewHolder>() {

    // Infla el layout de cada ítem del RecyclerView y crea un ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonajeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personaje, parent, false)
        return PersonajeViewHolder(view)
    }

    // Vincula los datos con la vista del ViewHolder en la posición específica
    override fun onBindViewHolder(holder: PersonajeViewHolder, position: Int) {
        val imagenResId = personajes[position] // Obtiene el ID de la imagen en la posición actual
        holder.bind(imagenResId) // Llama al método para asignar la imagen y configurar la animación
    }

    // Devuelve la cantidad de elementos en la lista
    override fun getItemCount(): Int = personajes.size

    // Clase interna que representa cada ítem en el RecyclerView
    inner class PersonajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imagenPersonaje: ImageView = view.findViewById(R.id.imagenPersonaje)

        // Método para asignar la imagen y definir el comportamiento al hacer clic
        fun bind(imagenResId: Int) {
            imagenPersonaje.setImageResource(imagenResId) // Asigna la imagen al ImageView

            itemView.setOnClickListener {
                // Carga la animación desde los recursos
                val animAmplio = AnimationUtils.loadAnimation(itemView.context, R.anim.animacion_personaje)

                // Listener para ejecutar una acción cuando la animación termine
                animAmplio.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        onItemClick(imagenResId) // Ejecuta la función de clic con la imagen seleccionada
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })

                // Inicia la animación en la imagen del personaje
                imagenPersonaje.startAnimation(animAmplio)
            }
        }
    }
}
