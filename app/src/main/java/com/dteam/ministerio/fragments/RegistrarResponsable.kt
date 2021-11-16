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
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ListaObservacionesAdaper
import com.dteam.ministerio.entities.Observacion
import com.dteam.ministerio.entities.Usuario
import com.dteam.ministerio.network.OrionApi
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
                    txtEmail.text.toString(),
                    OrionApi.USER_ENABLED
                )
                var password = txtDni.text.toString()
                registrarResponsable(usuario, password)
            }
        }

        return v
    }

    fun registrarResponsable(usuario:Usuario, password:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Registrar responsable")
        builder.setMessage("¿Desea confirmar el registro del responsable con mail ${usuario.email} ?")
        builder.setPositiveButton("Si") { dialogInterface: DialogInterface, i: Int ->
            usuarioViewModel.registrarUsuario(usuario, password)
            usuarioViewModel.usuarioRegistadoOk.observe(viewLifecycleOwner, Observer { list ->
                if (usuarioViewModel.usuarioRegistadoOk.value == true){
                    usuarioViewModel.cerrarSesion()
                    Snackbar.make(v,"Responsable registrado exitosamente", Snackbar.LENGTH_SHORT).show()
                    val action = RegistrarResponsableDirections.actionRegistrarResponsableToLogIn()
                    v.findNavController().navigate(action)
                }
                else{
                    Snackbar.make(v, usuarioViewModel.error, Snackbar.LENGTH_SHORT).show()
                }
            })

        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->

        }
        builder.show()

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