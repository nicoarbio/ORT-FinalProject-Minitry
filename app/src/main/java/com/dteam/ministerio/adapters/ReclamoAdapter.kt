package com.dteam.ministerio.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Reclamo
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


class ReclamoAdapter (var reclamoList : MutableList <Reclamo>,
                      var context :Context,
                      var onClick : (Int)->Unit) : RecyclerView.Adapter<ReclamoAdapter.ReclamoHolder>() {

    class ReclamoHolder (v: View) : RecyclerView.ViewHolder(v) {

        private var view: View

        init {
            this.view = v
        }

        fun setCategoria(categoria:String) {
            var lblCategoria: TextView = view.findViewById(R.id.lblCategoriaReclamo)
            lblCategoria.text = categoria
        }
        fun setSubCategoria(subCategoria:String) {
            var lblSubcategoria: TextView = view.findViewById(R.id.lblSubcategoriaReclamo)
            lblSubcategoria.text = subCategoria
        }
        fun setDireccion(direccion:String) {
            var lblDireccion: TextView = view.findViewById(R.id.lblDireccion)
            lblDireccion.text = direccion
        }
        fun getCardView() : CardView {
            return view.findViewById(R.id.card_reclamo_item)
        }
        fun getImageReclamo() : ImageView{
         return  view.findViewById(R.id.imgReclamo)
        }
        fun getImageEstado() : ImageView{
            return  view.findViewById(R.id.imgEstado)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReclamoHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_reclamo_list,parent,false)
        return (ReclamoHolder(view))
    }

    override fun onBindViewHolder(holder: ReclamoHolder, position: Int) {
        holder.setCategoria(reclamoList[position].categoria)
        holder.setSubCategoria(reclamoList[position].subCategoria)
        holder.setDireccion(reclamoList[position].direccion)

        val storage = FirebaseStorage.getInstance()// Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage.getReferenceFromUrl("gs://ort-proyectofinal.appspot.com/")
        val imgReclamo = gsReference.child("categorias").child(reclamoList[position].categoria + ".png")
        val imgEstado = gsReference.child("estados").child(reclamoList[position].estado + ".png")

        var cardImageReclamo : ImageView =  holder.getImageReclamo()
        Glide.with(context)
            .load(imgReclamo)
            .into(cardImageReclamo)

        var cardImageEstado : ImageView =  holder.getImageEstado()
        Glide.with(context)
            .load(imgEstado)
            .into(cardImageEstado)

        holder.getCardView().setOnClickListener(){
            onClick(position)
        }


    }

    override fun getItemCount(): Int {
        Log.d("Test", reclamoList.size.toString())
        return reclamoList.size
    }
}