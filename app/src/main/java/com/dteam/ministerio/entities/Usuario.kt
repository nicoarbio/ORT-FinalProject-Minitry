package com.dteam.ministerio.entities

import com.google.firebase.firestore.DocumentId

class Usuario(email: String, contrasenia: String, direccion: String, rol:  String, nombre: String, apellido: String, dni:  String, fechaDeNacimiento: String, telefono: String, codigoPostal: String) {
    @DocumentId
    private val documentId: String? = null
    var email : String
    var contrasenia: String
    var rol: String
    var nombre: String
    var apellido: String
    var dni: String
    var fechaDeNacimiento: String

    var telefono: String
    var direccion: String
    var codigoPostal : String
    var reclamos : MutableList<Reclamo>

    constructor(email: String, contrasenia: String, direccion: String) : this(email, contrasenia, direccion, "","","","", "", "","")
    constructor() : this("","","","","","","", "", "", "")


    init {
        this.email = email
        this.contrasenia = contrasenia
        this.rol = rol
        this.nombre = nombre
        this.apellido = apellido
        this.dni = dni
        this.fechaDeNacimiento = fechaDeNacimiento
        this.telefono = telefono
        this.direccion = direccion
        this.codigoPostal = codigoPostal
        this.reclamos = mutableListOf()
    }

    override fun toString(): String {
        return "Usuario(documentId=$documentId, email='$email', contrasenia='$contrasenia', rol='$rol', nombre='$nombre', apellido='$apellido', dni='$dni', fechaDeNacimiento=$fechaDeNacimiento, telefono='$telefono', direccion='$direccion', codigoPostal='$codigoPostal', reclamos=$reclamos)"
    }


}