package com.dteam.ministerio.entities

import com.google.firebase.firestore.DocumentId




class Categoria(
    nombre : String
) {
    var nombre: String
    var subcategorias: MutableList<Subcategoria>
    @DocumentId
    val documentId: String? = null
    constructor() : this("")

    init {
        this.nombre = nombre
        this.subcategorias = mutableListOf()
    }

    override fun toString(): String {
        return "Categoria(nombre='$nombre', subcategorias=$subcategorias, documentId=$documentId)"
    }
}