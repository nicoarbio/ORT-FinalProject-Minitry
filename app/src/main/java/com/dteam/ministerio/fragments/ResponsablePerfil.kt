package com.dteam.ministerio.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dteam.ministerio.R
import com.dteam.ministerio.network.OrionApi
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar

class ResponsablePerfil : Fragment() {

    companion object {
        fun newInstance() = ResponsablePerfil()
    }

    lateinit var v: View
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var txtNombreApellido : TextView
    private lateinit var txtEmail : TextView
    private lateinit var txtDni : TextView
    private lateinit var txtTelefono : TextView
    private lateinit var btnEditarResponsable : Button
    private lateinit var btnEliminarResponsable : Button
    private lateinit var btnCerrarSesion : Button

    private var rol: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.responsable_perfil_fragment, container, false)

        txtNombreApellido = v.findViewById(R.id.lblNombreYApellidoPerfil)
        txtEmail = v.findViewById(R.id.lblEmailPerfil)
        txtDni = v.findViewById(R.id.lblDni)
        txtTelefono = v.findViewById(R.id.lblTelefonoPerfil)
        btnEditarResponsable = v.findViewById(R.id.btnEditarResponsable)
        btnEliminarResponsable = v.findViewById(R.id.btnEliminarResponsable)
        btnCerrarSesion = v.findViewById(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener{
            usuarioViewModel.usuarioLogueadoOk.value=false
            usuarioViewModel.error=""
            usuarioViewModel.cerrarSesion()
            val action = ResponsablePerfilDirections.actionResponsablePerfilToLogIn()
            v.findNavController().navigate(action)
        }

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usuarioViewModel = ViewModelProvider(requireActivity()).get(UsuarioViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        try {
            rol = ResponsablePerfilArgs.fromBundle(requireArguments()).rol
        } catch (e:Exception) {
            rol = null
        }

        if (rol == null) {
            //Si se accede desde la bottom nav bar
            usuarioViewModel.actualizarUsuarioLogueado()

            btnEditarResponsable.visibility = View.GONE
            btnEliminarResponsable.visibility = View.GONE
        }else{
            btnCerrarSesion.visibility = View.GONE
        }
        setObserver()

    }

    fun setObserver(){
        usuarioViewModel.usuario.observe(viewLifecycleOwner, Observer { user ->
            txtNombreApellido.text = user.nombre +" "+user.apellido
            txtTelefono.text = user.telefono
            txtDni.text = user.dni
            txtEmail.text = user.email

            if (rol != null){
                btnEditarResponsable.setOnClickListener{
                    // DONE: Boton editar responsable
                    val actionIrAeditar = ResponsablePerfilDirections.actionResponsablePerfilToEditarResponsable()
                    v.findNavController().navigate(actionIrAeditar)
                }

                btnEliminarResponsable.setOnClickListener{
                    showDialogEliminarResponsable(user.documentId)
                }
            }
        })
    }

    fun showDialogEliminarResponsable(UID : String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Eliminar Responsable")

        builder.setPositiveButton("Eliminar", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            //usuarioViewModel.eliminarUsuario(UID)
            usuarioViewModel.usuario.value!!.isEnabled = OrionApi.USER_DISABLED
            usuarioViewModel.actualizarUsuario(UID, usuarioViewModel.usuario.value!!)

            usuarioViewModel.usuarioModificadoOk.observe(viewLifecycleOwner, Observer{ eliminado ->
                if(eliminado == true){
                    Snackbar.make(v,"Eliminado. Verifique los reclamos asignados a este responsable", Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(v,R.string.errorGeneral, Snackbar.LENGTH_SHORT).show()
                }
            })
        })

        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()

    }

}