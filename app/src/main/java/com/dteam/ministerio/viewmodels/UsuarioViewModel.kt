package com.dteam.ministerio.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dteam.ministerio.entities.Usuario
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dteam.ministerio.SingleLiveEvent
import com.dteam.ministerio.network.OrionApi
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UsuarioViewModel : ViewModel() {

    var usuario = SingleLiveEvent<Usuario>()
    var usuarioRegistadoOk = SingleLiveEvent<Boolean>()
    var usuarioLogueadoOk = SingleLiveEvent<Boolean>()
    var usuarioEliminadoOk = SingleLiveEvent<Boolean>()
    var usuarioModificadoOk = SingleLiveEvent<Boolean>()
    var usuariosResponsables = SingleLiveEvent<MutableList<Usuario>>()
    var usuarioRol = SingleLiveEvent<String>()
    var error = String()

    private var  auth: FirebaseAuth? = null

    init {
        auth = FirebaseAuth.getInstance()
        usuarioRol.value = ""
        usuario.value = Usuario()
    }

    fun registrarUsuario(user: Usuario, pwd : String) {
        viewModelScope.launch {
            try {
                OrionApi.retrofitService.verificarConexion()
                auth!!.createUserWithEmailAndPassword(user.email, pwd).await()
                user.documentId = auth!!.uid!!
                OrionApi.retrofitService.registrarUsuario(user)
                usuario.value = user
                Log.d("ORION_API", "Usuario registrado correctamente: " + usuario.value.toString())
                usuarioRegistadoOk.value = true
            }catch (e : FirebaseAuthWeakPasswordException){
                error = "La contraseña debe tener al menos 6 caracteres"
                usuarioRegistadoOk.value = false
            }catch (e : FirebaseAuthInvalidCredentialsException){
                error = "El mail ingresado debe tener un formato válido"
                usuarioRegistadoOk.value = false
            }catch (e : FirebaseAuthUserCollisionException) {
                error = "El mail ingresado ya se encuentra registrado"
                usuarioRegistadoOk.value = false
            }catch (e : Exception){
                error = "Ocurrió un error al registrar el usuario. Vuelva a intentar más tarde"
                usuarioRegistadoOk.value = false
                Log.d("ORION_API", e.toString())
            }
        }
    }

    fun obtenerUsuarioLogueado(): FirebaseUser? {
        return auth?.currentUser
    }

    fun iniciarSesion(mail:String, password:String){
        viewModelScope.launch {
            try {
                var posibleCiudadano = getUsuarioByEmail(mail)

                if (posibleCiudadano != null && posibleCiudadano.rol != "Ciudadano") {
                    auth!!.signInWithEmailAndPassword(mail, password)
                        .await()
                    usuario.value = OrionApi.retrofitService.getUsuarioByUID(auth!!.uid!!)
                } else {
                    throw Exception("Como si el usuario no existiera. Parametros incorrectos")
                }
                usuarioLogueadoOk.value = true
            }catch(e : Exception){
                error = "Usuario y/o contraseña incorrectos"
                usuarioLogueadoOk.value = false
            }
        }
    }

    fun actualizarUsuarioRolLogueado() {
        viewModelScope.launch {
            try {
                usuarioRol.value = OrionApi.retrofitService.getUsuarioByUID(auth!!.uid!!).rol
            } catch (e: Exception) {
                Log.d("ORION_API", e.toString())
            }
        }
    }

    fun actualizarUsuarioLogueado() {
        try {
            getUsuarioByUID(obtenerUsuarioLogueado()!!.uid)
        } catch (e: Exception) {
            error = "No se pudo obtener los datos del usuario"
            Log.d("ORION_API", e.toString())
        }

    }

    suspend fun getUsuarioByEmail(email:String) : Usuario? {
        try {
            val listaAux = OrionApi.retrofitService.getUsuarioByEmail("email:"+email)
            return listaAux.find {
                usr -> usr.email == email
            }
        }catch (e:Exception) {
            Log.d("ORION_API", e.toString())
            return null
        }
    }

    fun cerrarSesion(){
        auth?.signOut()
    }

    fun eliminarUsuario(UID: String){ //NO elimina de Firebase (no es necesario). Solo de Orion.
        viewModelScope.launch {
            try {
                OrionApi.retrofitService.eliminarUsuario(UID)
                usuarioEliminadoOk.value = true
            }catch (e : Exception){
                error = "No se pudo eliminar el usuario"
                usuarioEliminadoOk.value = false
                Log.d("usuarioViewModel", e.toString())
            }
        }
    }

    fun actualizarUsuario(UID: String, usuarioModificado: Usuario){
        viewModelScope.launch {
            try {
                OrionApi.retrofitService.actualizarUsuario(UID, usuarioModificado)
                usuarioModificadoOk.value = true
            }catch (e : Exception){
                error = "No se pudo modificar el usuario"
                usuarioModificadoOk.value = false
                Log.d("usuarioViewModel", e.toString())
            }
        }
    }

    fun getUsuarioByUID(UID : String) {
        viewModelScope.launch {
            try {
                usuario.value = OrionApi.retrofitService.getUsuarioByUID(UID)
            } catch (e: Exception) {
                Log.d("ORION_API_errorGetUsrUID", e.toString())
            }
        }
    }

    // getUsuariosResponsables -> lista de usuarios cuyo rol sea responsable
    fun getUsuariosResponsables() {
        viewModelScope.launch {
            try {
                usuariosResponsables.value = OrionApi.retrofitService.getUsuariosResponsables().toMutableList()
            } catch (e: Exception) {
                Log.d("ORION_API", e.toString())
            }
        }
    }



    fun setEmail(email : String){
        usuario.value?.email = email
    }
    fun setNombre(nombre: String) {
        usuario.value?.nombre = nombre
    }
    fun setApellido(apellido: String) {
         usuario.value?.apellido = apellido
    }
    fun setDni(dni : String) {
        usuario.value?.dni = dni
    }

}