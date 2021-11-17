package com.dteam.ministerio.entities

import com.dteam.ministerio.R
import com.dteam.ministerio.network.OrionApi
import com.dteam.ministerio.network.OrionApiService
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
    var email: String,
    var fotoPerfil: String,
    var isEnabled : String
)
{
    constructor() : this("","","","","","", "", "", "","","", "", OrionApi.USER_ENABLED)
    constructor(type:String, rol:String, nombre: String,apellido: String, dni: String,email: String,isEnabled: String, telefono: String) : this("", type, dni, apellido, "", "", "", nombre,rol,telefono,email,"",isEnabled)

    override fun toString(): String {
        return "Usuario(documentId='$documentId', type='$type', dni='$dni', apellido='$apellido', codigoPostal='$codigoPostal', direccion='$direccion', fechaDeNacimiento='$fechaDeNacimiento', nombre='$nombre', rol='$rol', telefono='$telefono', email='$email',fotoPerfil='$fotoPerfil', isEnabled='$isEnabled')"
    }
}