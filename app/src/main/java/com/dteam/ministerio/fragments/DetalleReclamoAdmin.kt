package com.dteam.ministerio.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ImgReclamoAdapter
import com.dteam.ministerio.adapters.ListaObservacionesAdaper
import com.dteam.ministerio.entities.Observacion
import com.dteam.ministerio.viewmodels.DetalleReclamoViewModel
import com.dteam.ministerio.viewmodels.ReclamoViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetalleReclamoAdmin : Fragment() {

    companion object {
        fun newInstance() = DetalleReclamoAdmin()
    }

    private lateinit var reclamoViewModel: ReclamoViewModel

    lateinit var v: View
    private lateinit var imgDetalleCategoria: ImageView
    private lateinit var txtDetalleCategoria: TextView
    private lateinit var txtDetalleSubCategoria: TextView

    private lateinit var txtDetalleDireccion: TextView
    private lateinit var txtDetalleComentario: TextView

    private lateinit var txtEstadoReclamo: TextView

    private lateinit var recImgReclamo: RecyclerView
    private lateinit var recDetalleObservaciones: RecyclerView

    private lateinit var btnDetalleAgregarObser: Button

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
        recDetalleObservaciones = v.findViewById(R.id.recDetalleObservaciones)

        btnDetalleAgregarObser = v.findViewById(R.id.btnDetalleAgregarObser)
        btnDetalleAgregarObser.setOnClickListener{
            showdialog()
        }
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reclamoViewModel = ViewModelProvider(requireActivity()).get(ReclamoViewModel::class.java)
        // TODO: Use the ViewModel
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

        recDetalleObservaciones.setHasFixedSize(true)
        recDetalleObservaciones.layoutManager = LinearLayoutManager(context)

        setObserver()
    }

    fun setObserver(){
        reclamoViewModel.reclamo.observe(viewLifecycleOwner, Observer {
            txtDetalleCategoria.text = it.categoria
            txtDetalleSubCategoria.text = it.subCategoria
            txtDetalleDireccion.text = it.direccion
            txtDetalleComentario.text = it.descripcion
            txtEstadoReclamo.text = it.estado
            recDetalleObservaciones.adapter = ListaObservacionesAdaper(it.observaciones)
            recImgReclamo.adapter = ImgReclamoAdapter(it.imagenes, requireContext())
        })
    }

    fun showdialog(){
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
            var obsNuevo = Observacion("usuario", texto, fecha)
            if (texto.length > 0 && reclamoViewModel.agregarObser(obsNuevo)) {
                //obs generado con exito
                Snackbar.make(v,"se agregó la observación", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(v,"Ocurrió un error. Vuelva a intentar mas tarde", Snackbar.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()

    }


}