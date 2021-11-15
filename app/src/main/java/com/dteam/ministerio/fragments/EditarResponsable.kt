package com.dteam.ministerio.fragments

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Usuario
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar

class EditarResponsable : Fragment() {

    companion object {
        fun newInstance() = EditarResponsable()
    }
    lateinit var v: View
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var btnGuardarCambios : Button
    private lateinit var txtNombre : EditText
    private lateinit var txtApellido : EditText
    private lateinit var txtDni : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.editar_responsable_fragment, container, false)
        txtNombre = v.findViewById(R.id.editNombre)
        txtApellido = v.findViewById(R.id.editApellido)
        txtDni = v.findViewById(R.id.editDni)
        txtNombre.setText(usuarioViewModel.usuario.value!!.nombre)
        txtApellido.setText(usuarioViewModel.usuario.value!!.apellido)
        txtDni.setText(usuarioViewModel.usuario.value!!.dni)
        btnGuardarCambios = v.findViewById(R.id.btnEditarResponsable)
        btnGuardarCambios.setOnClickListener(){
            if(validarCampos(txtNombre, txtApellido, txtDni)){

                var usuario = Usuario(
                    "Usuario",
                    "Responsable",
                    txtNombre.text.toString(),
                    txtApellido.text.toString(),
                    txtDni.text.toString(),
                    usuarioViewModel.usuario.value!!.email
                )
                usuarioViewModel.actualizarUsuario(usuario)
                usuarioViewModel.usuarioModificadoOk.observe(viewLifecycleOwner, Observer { list ->
                    if (usuarioViewModel.usuarioModificadoOk.value == true){
                        val action = EditarResponsableDirections.actionEditarResponsableToResponsablePerfil2(null)
                        v.findNavController().navigate(action)
                    }
                    else{
                        Snackbar.make(v, usuarioViewModel.error, Snackbar.LENGTH_SHORT).show()
                    }
                })
            }
        }
        return v
    }

    fun validarCampos(vararg campos:EditText):Boolean{
        var camposValidos = true
        for (campo in campos) {
            if(campo.text.isEmpty()){
                camposValidos = false
                campo.setError(getString(R.string.campoVacio))
            }
        }
        return camposValidos
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
    }
}