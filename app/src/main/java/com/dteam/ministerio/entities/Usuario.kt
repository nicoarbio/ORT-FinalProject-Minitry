package com.dteam.ministerio.entities

import com.squareup.moshi.Json

class Usuario(
    @Json(name = "id")
    var documentId : String,
    var type:String,
    var dni:  String,
    var apellido: String,
    var codigoPostal: String,
    var direccion: String,
    var fechaDeNacimiento: String,
    var nombre: String,
    var rol: String, //Ciudadano/Ministerio/Responsable
    var telefono: String,
    var email: String
)
{
    constructor() : this("","","","","","", "", "", "","","")
    constructor(uid:String) : this(uid,"","","","","", "", "", "","","")
    constructor(type:String, rol:String, nombre:String, apellido:String, fnac:String, dni:String, telefono:String, email:String, direccion:String)  : this("",type,dni,apellido,"",direccion, fnac, nombre, rol,telefono,email)
    constructor(type:String, rol:String, nombre: String,apellido: String, dni: String,email: String) : this("", type, dni, apellido, "", "", "", nombre,rol,"",email)

    override fun toString(): String {
        return "Usuario(documentId='$documentId', type='$type', dni='$dni', apellido='$apellido', codigoPostal='$codigoPostal', direccion='$direccion', fechaDeNacimiento='$fechaDeNacimiento', nombre='$nombre', rol='$rol', telefono='$telefono', email='$email')"
    }
}