package com.dteam.ministerio.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ImgReclamoAdapter
import com.dteam.ministerio.adapters.ListaObservacionesAdaper
import com.dteam.ministerio.entities.Observacion
import com.dteam.ministerio.viewmodels.ReclamoViewModel
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetalleReclamoAdmin : Fragment() {

    companion object {
        fun newInstance() = DetalleReclamoAdmin()
    }

    private lateinit var reclamoViewModel: ReclamoViewModel
    private lateinit var usuarioViewModel: UsuarioViewModel

    lateinit var v: View
    private lateinit var imgDetalleCategoria: ImageView
    private lateinit var txtDetalleCategoria: TextView
    private lateinit var txtDetalleSubCategoria: TextView

    private lateinit var txtDetalleDireccion: TextView
    private lateinit var txtDetalleComentario: TextView

    private lateinit var txtEstadoReclamo: TextView

    private lateinit var recImgReclamo: RecyclerView
    private lateinit var recDetalleObservaciones: RecyclerView

    private lateinit var lblImg: TextView

    private lateinit var btnDetalleAgregarObser: Button
    private lateinit var btnDetalleCancelarReclamo: ImageButton
    private lateinit var btnDetalleAsignarResp: Button

    private lateinit var txtCerrarReclamo: String

    private var rolUser : String? = null //Contiene el rol

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.detalle_reclamo_fragment, container, false)

        imgDetalleCategoria = v.findViewById(R.id.imgDetalleCategoria)
        txtDetalleCategoria = v.findViewById(R.id.txtDetalleCategoria)
        txtDetalleSubCategoria = v.findViewById(R.id.txtDetalleSubCategoria)

        txtDetalleDireccion = v.findViewById(R.id.txtDetalleDireccion)
        txtDetalleComentario = v.findViewById(R.id.txtDetalleComentario)

        txtEstadoReclamo = v.findViewById(R.id.txtEstadoReclamo)

        recImgReclamo = v.findViewById(R.id.recImgReclamo)
        lblImg = v.findViewById(R.id.lblDetalleImgAdjuntas)
        recDetalleObservaciones = v.findViewById(R.id.recDetalleObservaciones)

        btnDetalleAgregarObser = v.findViewById(R.id.btnDetalleAgregarObser)
        btnDetalleAsignarResp = v.findViewById(R.id.btnDetalleAsignarResp)

        btnDetalleAgregarObser.setOnClickListener{
            showdialogAgregarObser()
        }

        btnDetalleAsignarResp = v.findViewById(R.id.btnDetalleAsignarResp)
        btnDetalleCancelarReclamo = v.findViewById(R.id.btnDetalleCancelarReclamo)

        return v
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reclamoViewModel = ViewModelProvider(requireActivity()).get(ReclamoViewModel::class.java)
        usuarioViewModel = ViewModelProvider(requireActivity()).get(UsuarioViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        val storage = FirebaseStorage.getInstance()// Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage.getReferenceFromUrl("gs://ort-proyectofinal.appspot.com/")
        val imgReclamo = gsReference.child("categorias").child(reclamoViewModel.getCategoria() + ".png")
        Glide.with(this)
            .load(imgReclamo)
            .into(imgDetalleCategoria)

        recImgReclamo.setHasFixedSize(true)
        recImgReclamo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recImgReclamo.adapter = ImgReclamoAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}

        recDetalleObservaciones.setHasFixedSize(true)
        recDetalleObservaciones.layoutManager = LinearLayoutManager(context)

        setObserver()
        usuarioViewModel.actualizarUsuarioRolLogueado()
    }

    fun setObserver(){
        usuarioViewModel.usuarioRol.observe(viewLifecycleOwner, Observer {
            rolUser = it
            if (it == "Admin") {

                btnDetalleAsignarResp.setOnClickListener{
                    if(txtEstadoReclamo.text == "Asignado"){
                        showdialogReasignarResponsable()
                    } else{
                        val actionToListaRespon = DetalleReclamoAdminDirections.actionDetalleReclamoAdminToResponsableList("ASIGNAR")
                        v.findNavController().navigate(actionToListaRespon)
                    }
                }

                btnDetalleCancelarReclamo.setOnClickListener{
                    showdialogCancelarReclamo()
                }

            } else {
                txtCerrarReclamo = "CERRAR RECLAMO"
                btnDetalleAsignarResp.text = txtCerrarReclamo
                btnDetalleAsignarResp.setOnClickListener{
                    showdialogCerrarReclamo()
                }
                btnDetalleCancelarReclamo.visibility = View.GONE
            }
        })

        reclamoViewModel.reclamo.observe(viewLifecycleOwner, Observer {

            txtDetalleCategoria.text = it.categoria
            txtDetalleSubCategoria.text = it.subCategoria
            txtDetalleDireccion.text = it.direccion
            txtDetalleComentario.text = it.descripcion
            txtEstadoReclamo.text = it.estado
            recDetalleObservaciones.adapter = ListaObservacionesAdaper(it.observaciones)
            recImgReclamo.adapter = ImgReclamoAdapter(it.imagenes, requireContext()) { pos -> onItemClick(pos)}

            reclamoViewModel.getImgEstado()
            reclamoViewModel.imgEstadoReclamo.observe(viewLifecycleOwner, Observer {
                var imgEstado : ImageView =  v.findViewById(R.id.imgEstadoDetalleReclamo)
                Glide.with(this)
                    .load(it)
                    .into(imgEstado)
            })

            if( it.estado == "Cancelado" || it.estado == "Cerrado"){

                btnDetalleAgregarObser.visibility = View.GONE

                btnDetalleAsignarResp.visibility = View.GONE

                btnDetalleCancelarReclamo.visibility = View.GONE

            }
            if(it.imagenes.size ==0){
                lblImg.visibility = View.GONE
                recImgReclamo.visibility = View.GONE
            }
        })



    }

    fun showdialogAgregarObser(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Agregar Observación")

        val input = EditText(this.context)
        input.setHint("Ingrese su Observación")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Agregar", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            var texto = input.text.toString()
            val currentDateTime = LocalDateTime.now()
            val fecha = currentDateTime.format(DateTimeFormatter.ISO_DATE)
            //var obsNuevo = Observacion("Ministerio", texto, fecha)
            var obsNuevo = Observacion(rolUser!!, texto, fecha)

            if (input.length() > 0) {
                reclamoViewModel.agregarObser(obsNuevo)
                reclamoViewModel.estadoGuardadoObsOk.observe(viewLifecycleOwner, Observer{list ->
                    if(reclamoViewModel.estadoGuardadoObsOk.value==true){
                        recDetalleObservaciones.adapter = ListaObservacionesAdaper(reclamoViewModel.getObservaciones()!!)
                        Snackbar.make(v,"se agregó la observación", Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(v,R.string.errorGeneral, Snackbar.LENGTH_SHORT).show()
                    }
                })
            }else{
                Snackbar.make(v,"No se puede agregar una observación vacía", Snackbar.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()

    }

    fun showdialogCancelarReclamo() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Cancelar Reclamo")

        val input = EditText(this.context)
        input.setHint("Ingrese el motivo")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            var motivo = input.text.toString()
            if (input.length() > 0) {
                var obserNuevo = Observacion("Ministerio", "Tu reclamo ha sido cancelado. Motivo: $motivo", getFecha())
                reclamoViewModel.cancelarReclamo(obserNuevo)
                reclamoViewModel.estadoGuardadoOk.observe(viewLifecycleOwner, Observer{list ->
                    if(reclamoViewModel.estadoGuardadoOk.value==true){
                        txtEstadoReclamo.text = reclamoViewModel.getEstado()

                        Snackbar.make(v,"Se canceló el Reclamo", Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(v,R.string.errorGeneral, Snackbar.LENGTH_SHORT).show()
                    }
                })
            }else{
                Snackbar.make(v,"Error, ingrese un motivo de la cancelación", Snackbar.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    fun showdialogCerrarReclamo() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Cerrar el Reclamo")

        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            var obserNuevo = Observacion("Responsable", "Reclamo Cerrado", getFecha())
            reclamoViewModel.cerrarReclamo(obserNuevo)
            reclamoViewModel.estadoGuardadoOk.observe(viewLifecycleOwner, Observer{list ->
                if(reclamoViewModel.estadoGuardadoOk.value==true){
                    txtEstadoReclamo.text = reclamoViewModel.getEstado()
                    Snackbar.make(v,"Se cerró el Reclamo", Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(v,R.string.errorGeneral, Snackbar.LENGTH_SHORT).show()
                }
            })
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    fun showdialogReasignarResponsable() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)

        builder.setTitle("Reasignar Responsable")
        builder.setMessage("El reclamo seleccionado ya se encuentra asignado a otro usuario. ¿Desea continuar de todos modos?")

        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val actionToListaRespon = DetalleReclamoAdminDirections.actionDetalleReclamoAdminToResponsableList("ASIGNAR")
            v.findNavController().navigate(actionToListaRespon)
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun getFecha(): String {
        val currentDateTime = LocalDateTime.now()
        return currentDateTime.format(DateTimeFormatter.ISO_DATE)
    }

    fun onItemClick(pos: Int){
        showImage(reclamoViewModel.reclamo.value!!.imagenes[pos])
    }

    fun showImage(imageUriSting : String) {
        val builder = Dialog(this.requireContext(), android.R.style.Theme_Light)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(
            ColorDrawable(Color.BLACK)
        )
        builder.setOnDismissListener(DialogInterface.OnDismissListener {
            //nothing;
        })
        val imageView = ImageView(this.requireContext())
        var imageUri = Uri.parse(imageUriSting)
        Glide.with(this)
            .load(imageUri)
            .into(imageView)
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        )
        builder.show()
    }

}