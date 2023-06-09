package com.dteam.ministerio.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Usuario
import com.google.firebase.storage.FirebaseStorage

class ListaResponsableAdapter (var responsableList: MutableList <Usuario>,
                               var context: Context,
                               var onClick: (Int)->Unit) : RecyclerView.Adapter<ListaResponsableAdapter.ResponsableHolder>() {

    class ResponsableHolder (v : View): RecyclerView.ViewHolder(v){
        private var view: View

        init {
            this.view = v
        }

        fun setNombre(nombre:String) {
            var lblNombre: TextView = view.findViewById(R.id.lblNombreRespon)
            lblNombre.text = nombre
        }

        fun setDni(dni: String){
            var lblDni: TextView = view.findViewById((R.id.lblDniRespon))
            lblDni.text = dni
        }

        fun getCardView() : CardView {
            return view.findViewById(R.id.card_respon_item)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponsableHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_responsable_list,parent,false)
        return (ResponsableHolder(view))
    }

    override fun onBindViewHolder(holder: ResponsableHolder, position: Int) {
        var nombreApellido = responsableList[position].nombre + " " + responsableList[position].apellido
        holder.setNombre(nombreApellido)
        holder.setDni(responsableList[position].dni)

        holder.getCardView().setOnClickListener(){
            onClick(position)
        }

    }

    override fun getItemCount(): Int {
        return responsableList.size
    }
}