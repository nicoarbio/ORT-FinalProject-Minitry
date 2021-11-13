package com.dteam.ministerio.viewmodels

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.dteam.ministerio.entities.Subcategoria
import com.dteam.ministerio.entities.Observacion
import com.dteam.ministerio.entities.Reclamo
import com.dteam.ministerio.entities.Usuario
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import kotlin.math.log

class ReclamoViewModel : ViewModel() {

    val db = Firebase.firestore
    private var reclamoList : MutableList<Reclamo> = mutableListOf()
    val listadoReclamos = MutableLiveData<MutableList<Reclamo>>()
    var reclamo = MutableLiveData<Reclamo>()

    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    var estadoGuardadoOk = MutableLiveData<Boolean>()

    init {
        reclamo.value= Reclamo()
    }

    fun generarReclamo(reclamoNuevo : Reclamo, imagenes:List<Uri>):Boolean {
        var reclamoGenerado = true
        try {
            viewModelScope.launch {
                var uploadTask: UploadTask
                for (img in imagenes) {
                    val imgReclamo = storageRef.child("reclamos/${img.lastPathSegment}")
                    uploadTask = imgReclamo.putFile(img)
                    uploadTask.await()
                    if (uploadTask.isSuccessful()) {
                        var url = storageRef.child("reclamos/${img.lastPathSegment}").downloadUrl.await()
                        reclamoNuevo.imagenes.add(url.toString())
                    }

                    Log.d("test", "Dentro task succesful: " + reclamoNuevo.toString())
                }
                Log.d("test", "Fuera de scope: " + reclamoNuevo.toString())
                reclamo.value=reclamoNuevo
                db.collection("reclamos")
                    .add(reclamoNuevo)
            }

        } catch (e: Exception) {
                Log.d("Test", "Error al generar reclamo: " + e)
                reclamoGenerado = false
            }
        return reclamoGenerado
    }

    fun getReclamos() {
         viewModelScope.launch {
             reclamoList.clear()
             try {
                 val reclamos = db.collection("reclamos")
                     .whereEqualTo("usuario", "UID_DEL_USUARIO") //TODO: Acá poner el UID del usuario logueado!
                     .get()
                     .await()
                 if (reclamos != null) {
                     for (reclamo in reclamos) {
                         reclamoList.add(reclamo.toObject())
                     }
                     listadoReclamos.value =  reclamoList
                 }
             }catch (e: Exception){
                 Log.w("Test", "Error al obtener documentos: ", e)
             }
         }
    }

    fun getReclamosPorEstado(estadoReclamo : String, responsableUID : String?) {
        viewModelScope.launch {
            reclamoList.clear()
            try {
                var reclamos : QuerySnapshot
                if(responsableUID==null){
                    reclamos = db.collection("reclamos")
                        .whereEqualTo("estado", estadoReclamo)
                        .get()
                        .await()
                }else{
                    reclamos = db.collection("reclamos")
                        .whereEqualTo("estado", estadoReclamo)
                        .whereEqualTo("responsable", responsableUID)
                        .get()
                        .await()
                }

                if (reclamos != null) {
                    for (reclamo in reclamos) {
                        reclamoList.add(reclamo.toObject())
                    }
                    listadoReclamos.value =  reclamoList
                }
            }catch (e: Exception){
                Log.w("Test", "Error al obtener documentos: ", e)
            }
        }
    }
    fun getReclamosPorCateg(subcateg : String) {
        viewModelScope.launch {
            reclamoList.clear()
            try {
                val reclamos = db.collection("reclamos")
                    .whereEqualTo("subCategoria", subcateg)//TODO: Acá poner el UID del usuario logueado!
                    .get()
                    .await()
                if (reclamos != null) {
                    for (reclamo in reclamos) {
                        reclamoList.add(reclamo.toObject())
                    }
                    listadoReclamos.value =  reclamoList
                }
            }catch (e: Exception){
                Log.w("Test", "Error al obtener documentos: ", e)
            }
        }
    }

    fun agregarObser(obserNuevo: Observacion) {
        viewModelScope.launch {
            try {
                // obtener el id del reclamo actual en la base de dato
                val ref = db.collection("reclamos").document(reclamo.value!!.documentId!!)
                ref.update("observaciones", FieldValue.arrayUnion(obserNuevo)).await()
                reclamo.value!!.observaciones.add(obserNuevo)
                estadoGuardadoOk.value = true
            } catch (e : Exception){
                estadoGuardadoOk.value = false
                Log.w("Test", "Error al  agregar observacion: ", e)
            }
        }

    }

    fun setEstado(estadoNuevo: String){
        viewModelScope.launch {
            try {
                // obtener el id del reclamo actual en la base de dato
                val ref = db.collection("reclamos").document(reclamo.value!!.documentId!!)
                ref.update("estado", estadoNuevo).await()
                reclamo.value!!.estado = estadoNuevo
                estadoGuardadoOk.value = true
            } catch (e : Exception){
                estadoGuardadoOk.value = false
                Log.w("Test", "Error al  cancelar el Reclamo: ", e)
            }
        }

    }

    fun setResponsable(respon: Usuario){
        viewModelScope.launch {
            try {
                // obtener el id del reclamo actual en la base de dato
                val ref = db.collection("reclamos").document(reclamo.value!!.documentId!!)
                ref.update("responsable", respon.documentId).await()
                reclamo.value!!.estado = "Asignado"
                estadoGuardadoOk.value = true
            } catch (e : Exception){
                estadoGuardadoOk.value = false
                Log.w("Test", "Error al  asignar el Responsable: ", e)
            }
        }

    }

    fun getCategoria(): String? {
        return reclamo.value?.categoria
    }
    fun getSubcategoria(): String? {
        return reclamo.value?.subCategoria
    }
    fun getDireccion(): String? {
        return reclamo.value?.direccion
    }
    fun getDescripcion(): String? {
        return reclamo.value?.descripcion
    }
    fun getEstado(): String? {
        return reclamo.value?.estado
    }
    fun getObservaciones(): MutableList<Observacion>? {
        return reclamo.value?.observaciones
    }
    fun setCategoria(categoria : String){
        reclamo.value!!.categoria=categoria
    }
    fun setSubcategoria(subcategoria: String){
        reclamo.value!!.subCategoria=subcategoria
    }
}