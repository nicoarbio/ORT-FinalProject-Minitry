package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Usuario
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar

class RegistrarResponsable : Fragment() {

    companion object {
        fun newInstance() = RegistrarResponsable()
    }

    private lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var txtEmail : EditText
    lateinit var txtNombre : EditText
    lateinit var txtApellido : EditText
    lateinit var txtDni : EditText
    lateinit var btnRegistrarse : Button
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.registrar_responsable_fragment, container, false)
        btnRegistrarse = v.findViewById(R.id.btnReg2)
        txtEmail = v.findViewById(R.id.ediEmail)
        txtNombre = v.findViewById(R.id.editNombre)
        txtApellido = v.findViewById(R.id.editApellido)
        txtDni = v.findViewById(R.id.editDni)

        btnRegistrarse.setOnClickListener{
            if(validarCampos(txtEmail, txtNombre, txtApellido, txtDni)){

                var usuario = Usuario(
                    "Usuario",
                    "Responsable",
                    txtNombre.text.toString(),
                    txtApellido.text.toString(),
                    txtDni.text.toString(),
                    txtEmail.text.toString()
                )
                var password = txtDni.text.toString()
                usuarioViewModel.registrarUsuario(usuario, password)
                usuarioViewModel.usuarioRegistadoOk.observe(viewLifecycleOwner, Observer { list ->
                    if (usuarioViewModel.usuarioRegistadoOk.value == true){
                        val action = RegistrarResponsableDirections.actionRegistrarResponsableToRegistroExitoso()
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