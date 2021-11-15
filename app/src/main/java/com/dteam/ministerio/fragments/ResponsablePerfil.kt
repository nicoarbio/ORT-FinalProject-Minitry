package com.dteam.ministerio.fragments

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
import com.dteam.ministerio.entities.Usuario
import com.dteam.ministerio.viewmodels.UsuarioViewModel

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
            usuarioViewModel.cerrarSesion()
            val action = ResponsablePerfilDirections.actionResponsablePerfilToLogIn()
            v.findNavController().navigate(action)
        }

        btnEditarResponsable.setOnClickListener{
            //TODO: Boton editar responsable
        }

        btnEliminarResponsable.setOnClickListener{
            //TODO: Boton eliminar responsable
            //Agregar atributo deshabilitar en orion
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
            //Admin
            usuarioViewModel.actualizarUsuarioLogueado()
            setObserver()
            btnEditarResponsable.visibility = View.GONE
            btnEliminarResponsable.visibility = View.GONE
        } else {
            //Responsable
            val responsable : Usuario = usuarioViewModel.usuario.value!!
            txtNombreApellido.text = responsable.nombre +" "+responsable.apellido
            txtTelefono.text = responsable.telefono
            txtDni.text = responsable.dni
            txtEmail.text = responsable.email
        }


    }

    fun setObserver(){
        usuarioViewModel.usuario.observe(viewLifecycleOwner, Observer {
            txtNombreApellido.text = it.nombre +" "+it.apellido
            txtTelefono.text = it.telefono
            txtDni.text = it.dni
            txtEmail.text = it.email
        })
    }

}