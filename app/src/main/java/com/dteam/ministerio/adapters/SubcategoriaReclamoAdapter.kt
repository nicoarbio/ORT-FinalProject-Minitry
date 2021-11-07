package com.dteam.ministerio.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Subcategoria
import com.dteam.ministerio.fragments.SubcategoriaReclamoListDirections

class SubcategoriaReclamoAdapter(
    var subCategoriaList: MutableList<Subcategoria>,
    var context: Context,
    var onClick: (Int)->Unit) : RecyclerView.Adapter<SubcategoriaReclamoAdapter.SubcategoriaReclamoHolder>()
{
    lateinit var view: View
    class SubcategoriaReclamoHolder  (v: View) : RecyclerView.ViewHolder(v)
    {
        private var view: View

            init {
                this.view = v
            }

        fun setSubcategoria(nombre:String){
            var txtSubcategoria : TextView = view.findViewById(R.id.lblSubcategoriaReclamoItem)
            txtSubcategoria.text = nombre
        }
        fun getCardView() : CardView {
                return view.findViewById(R.id.cardSubcategoriaReclamoItem)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoriaReclamoHolder {

        view =  LayoutInflater.from(parent.context).inflate(R.layout.subcategoria_reclamo_item,parent,false)
        return (SubcategoriaReclamoHolder(view))
    }
    override fun onBindViewHolder(holder: SubcategoriaReclamoHolder, position: Int) {
        holder.setSubcategoria(subCategoriaList[position].nombre)

        holder.getCardView().setOnClickListener(){
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return subCategoriaList.size
    }

}

