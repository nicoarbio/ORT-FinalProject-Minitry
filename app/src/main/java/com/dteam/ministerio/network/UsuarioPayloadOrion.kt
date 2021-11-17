package com.dteam.ministerio.network

import com.dteam.ministerio.entities.Usuario

class UsuarioPayloadOrion(
    var dni:  String,
    var apellido: String,
    var codigoPostal: String,
    var direccion: String,
    var fechaDeNacimiento: String,
    var nombre: String,
    var rol: String, //Ciudadano/Admin/Responsable
    var telefono: String,
    var email: String,
    var fotoPerfil: String,
    var isEnabled : String
)
{
    constructor(usuarioCompleto: Usuario) : this(
        usuarioCompleto.dni,
        usuarioCompleto.apellido,
        usuarioCompleto.codigoPostal,
        usuarioCompleto.direccion,
        usuarioCompleto.fechaDeNacimiento,
        usuarioCompleto.nombre,
        usuarioCompleto.rol,
        usuarioCompleto.telefono,
        usuarioCompleto.email,
        usuarioCompleto.fotoPerfil,
        usuarioCompleto.isEnabled
    )

    override fun toString(): String {
        return "UsuarioPaylodOrion(dni='$dni', apellido='$apellido', codigoPostal='$codigoPostal', direccion='$direccion', fechaDeNacimiento='$fechaDeNacimiento', nombre='$nombre', rol='$rol', telefono='$telefono', email='$email', fotoPerfil='$fotoPerfil', isEnabled='$isEnabled')"
    }


}