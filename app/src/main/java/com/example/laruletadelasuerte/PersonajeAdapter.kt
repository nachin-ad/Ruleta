package com.example.laruletadelasuerte

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PersonajeAdapter(
    private val personajes: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PersonajeAdapter.PersonajeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_personaje, parent, false)
        return PersonajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonajeViewHolder, position: Int) {
        val imagenResId = personajes[position]
        holder.bind(imagenResId)
    }

    override fun getItemCount(): Int = personajes.size

    inner class PersonajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imagenPersonaje: ImageView = view.findViewById(R.id.imagenPersonaje)

        fun bind(imagenResId: Int) {
            imagenPersonaje.setImageResource(imagenResId)
            itemView.setOnClickListener {
                val animAmplio = AnimationUtils.loadAnimation(itemView.context, R.anim.animacion_personaje)
                animAmplio.setAnimationListener(object: Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?){}

                    override fun onAnimationEnd(animation: Animation?){
                        onItemClick(imagenResId)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}

                })
                imagenPersonaje.startAnimation(animAmplio)
            }
        }
    }
}