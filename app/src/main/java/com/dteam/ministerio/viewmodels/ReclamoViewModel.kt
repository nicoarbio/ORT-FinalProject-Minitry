package com.dteam.ministerio.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.dteam.ministerio.SingleLiveEvent
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
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReclamoViewModel : ViewModel() {

    val db = Firebase.firestore
    private var reclamoList : MutableList<Reclamo> = mutableListOf()
    val listadoReclamos = MutableLiveData<MutableList<Reclamo>>()
    val reclamosFiltrados = SingleLiveEvent<MutableList<Reclamo>>()
    var reclamo = SingleLiveEvent<Reclamo>()

    val storage = FirebaseStorage.getInstance()
    var estadoGuardadoOk = SingleLiveEvent<Boolean>()

    var imgEstadoReclamo = SingleLiveEvent<Uri>()

    init {
        reclamo.value = Reclamo()
    }

    fun getReclamos() {
         viewModelScope.launch {
             reclamoList.clear()
             try {
                 val reclamos = db.collection("reclamos")
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

    inline infix fun <T> T?.isNullOr(predicate: (T) -> Boolean): Boolean = if (this != null) predicate(this) else true

    fun filtrarReclamos(responsable: String?, estado: String?, subCategoria: String?){
        reclamosFiltrados.value = reclamoList.filter { reclamo ->
            responsable isNullOr{reclamo.responsable==it} && estado isNullOr{reclamo.estado==it} && subCategoria isNullOr{reclamo.subCategoria==it}
        } as MutableList<Reclamo>
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

    fun cerrarReclamo(){
        viewModelScope.launch {
            try {
                // obtener el id del reclamo actual en la base de dato
                val ref = db.collection("reclamos").document(reclamo.value!!.documentId!!)
                ref.update("estado", "Cerrado").await()
                var obserNuevo = Observacion("Responsable",
                    "Reclamo Cerrado", getFecha())
                ref.update("observaciones", FieldValue.arrayUnion(obserNuevo)).await()
                reclamo.value!!.observaciones.add(obserNuevo)
                reclamo.value!!.estado = "Cerrado"
                estadoGuardadoOk.value = true
            } catch (e : Exception){
                estadoGuardadoOk.value = false
                Log.w("Test", "Error al cambiar el estado", e)
            }
        }
    }

    fun cancelarReclamo(motivo: String){
        viewModelScope.launch {
            try {
                // obtener el id del reclamo actual en la base de dato
                val ref = db.collection("reclamos").document(reclamo.value!!.documentId!!)
                ref.update("estado", "Cancelado").await()
                var obserNuevo = Observacion("Ministerio",
                    "Tu reclamo ha sido cancelado. Motivo: $motivo", getFecha())
                ref.update("observaciones", FieldValue.arrayUnion(obserNuevo)).await()
                reclamo.value!!.observaciones.add(obserNuevo)
                reclamo.value!!.estado = "Cancelado"
                estadoGuardadoOk.value = true
            } catch (e : Exception){
                estadoGuardadoOk.value = false
                Log.w("Test", "Error al cambiar el estado", e)
            }
        }
    }

    fun setResponsable(respon: Usuario){
        viewModelScope.launch {
            try {
                // obtener el id del reclamo actual en la base de dato
                val ref = db.collection("reclamos").document(reclamo.value!!.documentId!!)
                ref.update("responsable", respon.documentId).await()
                ref.update("estado", "Asignado").await()
                var obserNuevo = Observacion("Ministerio", respon.nombre + " " + respon.apellido+ " fue asignado para este reclamo", getFecha())
                ref.update("observaciones", FieldValue.arrayUnion(obserNuevo)).await()
                reclamo.value!!.observaciones.add(obserNuevo)
                reclamo.value!!.estado = "Asignado"
                reclamo.value = reclamo.value!!
                estadoGuardadoOk.value = true
            } catch (e : Exception){
                estadoGuardadoOk.value = false
                Log.w("Test", "Error al  asignar el Responsable: ", e)
            }
        }
    }

    fun getImgEstado(){
        viewModelScope.launch {
            val gsReference = storage.getReferenceFromUrl("gs://ort-proyectofinal.appspot.com/")
            val img = gsReference.child("estados").child(reclamo.value!!.estado + ".png").downloadUrl.await()
            imgEstadoReclamo.value = img
        }

    }

    private fun getFecha(): String {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(DateTimeFormatter.ISO_DATE)
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