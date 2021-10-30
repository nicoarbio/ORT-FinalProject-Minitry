package com.dteam.ministerio.entities

import java.util.*

class Observacion(autor: String, contenido: String, fecha: String){
    var autor: String
    var contenido: String
    var fecha: String

    constructor(): this("", "", "")

    init {
        this.autor = autor
        this.contenido = contenido
        this.fecha = fecha
    }
}