package com.dteam.ministerio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Observacion

class ListaObservacionesAdaper (var listaObservaciones: MutableList<Observacion>) :
    RecyclerView.Adapter<ListaObservacionesAdaper.ObservacionHolder>(){

    class ObservacionHolder (v: View) : RecyclerView.ViewHolder(v){

        private var view: View
        init {
            this.view = v
        }

        fun setAutor(autor: String){
            val txt: TextView = view.findViewById(R.id.txtObsAutor)
            txt.text = autor
        }

        fun setComentario(comentario: String){
            val txt: TextView = view.findViewById(R.id.txtObsComentario)
            txt.text = comentario
        }

        fun setFecha(fecha: String){
            val txt: TextView = view.findViewById(R.id.txtObsFecha)
            txt.text = fecha
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservacionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_observacion_list, parent, false)
        return(ObservacionHolder(view))
    }

    override fun onBindViewHolder(holder: ObservacionHolder, position: Int) {
        holder.setAutor(listaObservaciones[position].autor)
        holder.setComentario(listaObservaciones[position].contenido)
        holder.setFecha(listaObservaciones[position].fecha.toString())
    }

    override fun getItemCount(): Int {
        return listaObservaciones.size
    }
}