package com.example.laruletadelasuerte

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistorialAdapter(private val context: Context, private val partidas: List<Partida>) :
    RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtJugador1: TextView = view.findViewById(R.id.txtJugador1)
        val txtDinero1 : TextView = view.findViewById(R.id.txtDinero1)
        val txtJugador2: TextView = view.findViewById(R.id.txtJugador2)
        val txtDinero2 : TextView = view.findViewById(R.id.txtDinero2)
        val txtJugador3: TextView = view.findViewById(R.id.txtJugador3)
        val txtDinero3 : TextView = view.findViewById(R.id.txtDinero3)
        val txtGanador: TextView = view.findViewById(R.id.txtGanador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_historial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partida = partidas[position]
        holder.txtFecha.text = partida.fecha
        holder.txtJugador1.text = partida.nombreJugador1+":"
        holder.txtJugador2.text = partida.nombreJugador2+":"
        holder.txtJugador3.text = partida.nombreJugador3+":"

        // Formatear el dinero
        val dinero1 = formatMoney(partida.dineroJugador1)
        val dinero2 = formatMoney(partida.dineroJugador2)
        val dinero3 = formatMoney(partida.dineroJugador3)

        holder.txtDinero1.text = dinero1
        holder.txtDinero2.text = dinero2
        holder.txtDinero3.text = dinero3

        holder.txtGanador.text = "🏆 ${partida.ganador}"
    }

    fun formatMoney(dinero: Int): String {
        return "${"%,d".format(dinero)}€"
    }

    override fun getItemCount(): Int = partidas.size
}