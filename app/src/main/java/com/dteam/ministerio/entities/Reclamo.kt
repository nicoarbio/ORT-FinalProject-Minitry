package com.dteam.ministerio.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

class Reclamo(categoria: String, subCategoria: String, direccion: String, descripcion: String, usuario:String, estado: String, responsable: String?){
    @DocumentId
    var documentId: String? = null
    var categoria: String
    var subCategoria: String
    var direccion: String
    var descripcion: String
    var usuario: String
    var estado: String
    var responsable: String? = null
    var observaciones: MutableList<Observacion>
    var imagenes: ArrayList<String>

    constructor() : this("","","","","","","")
    constructor(categoria: String, subCategoria: String, direccion: String, descripcion: String, usuario: String, estado: String) : this(categoria, subCategoria, direccion, descripcion, usuario, estado, null)

    init {
        this.categoria = categoria
        this.subCategoria = subCategoria
        this.direccion = direccion
        this.descripcion = descripcion
        this.usuario = usuario
        this.estado= estado
        this.responsable= responsable
        this.observaciones = mutableListOf()
        this.imagenes = arrayListOf()
    }

    override fun toString(): String {
        return "Reclamo(categoria='$categoria', subCategoria='$subCategoria', direccion='$direccion', descripcion='$descripcion', usuario='$usuario', estado='$estado', responsable='$responsable', observaciones=$observaciones, imagenes=$imagenes)"
    }

}
