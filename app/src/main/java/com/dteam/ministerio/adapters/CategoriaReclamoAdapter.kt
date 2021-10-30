package com.dteam.ministerio.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Categoria
import com.google.firebase.storage.FirebaseStorage


class CategoriaReclamoAdapter (var categoriaList : MutableList <Categoria>,
                               var context :Context,
                               var onClick : (Int)->Unit) : RecyclerView.Adapter<CategoriaReclamoAdapter.CategoriaHolder>() {
    lateinit var view: View

    class CategoriaHolder (v: View) : RecyclerView.ViewHolder(v) {

        private var view: View

        init {
            this.view = v
        }

        fun setCategoria(categoria:String) {
            val lblCategoria: TextView = view.findViewById(R.id.lblTipoReclamo)
            lblCategoria.text = categoria
        }
        fun getCardView() : CardView {
            return view.findViewById(R.id.cardTipoReclamo)
        }
        fun getImageCategoria() : ImageView{
            return  view.findViewById(R.id.imgCategoria)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaHolder {
        view =  LayoutInflater.from(parent.context).inflate(R.layout.tipo_reclamo_item,parent,false)
        return (CategoriaHolder(view))
    }

    override fun onBindViewHolder(holder: CategoriaHolder, position: Int) {
        holder.setCategoria(categoriaList[position].nombre)

        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl("gs://ort-proyectofinal.appspot.com/")
        val imgCategoria = gsReference.child("categorias").child(categoriaList[position].nombre + ".png")

        val cardImageCategoria : ImageView =  holder.getImageCategoria()
        Glide.with(context)
            .load(imgCategoria)
            .into(cardImageCategoria)

        holder.getCardView().setOnClickListener(){
            onClick(position)

        }


    }

    override fun getItemCount(): Int {
        return categoriaList.size
    }
}