package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.dteam.ministerio.R
import com.dteam.ministerio.viewmodels.InicioAdminViewModel
import com.dteam.ministerio.viewmodels.UsuarioViewModel

class Inicio : Fragment() {

    companion object {
        fun newInstance() = Inicio()
    }

    lateinit var btn1 : Button
    lateinit var btn2 : Button
    lateinit var actionBtn1 : NavDirections
    lateinit var actionBtn2 : NavDirections
    lateinit var v : View


    private lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.inicio_fragment, container, false)
        btn1 = v.findViewById(R.id.btnInicio1)
        btn2 = v.findViewById(R.id.btnInicio2)
        return v
    }

    override fun onStart() {
        super.onStart()

        if (usuarioViewModel.getRol()=="Admin"){
            btn1.text = "Gestionar Reclamos"
            btn2.text = "Administrar responsables"
            actionBtn1 = InicioDirections.actionInicioToGestionarReclamos()
            actionBtn2 = InicioDirections.actionInicioToResponsableList("")
        }else{ //Si no es Admin, es responsable porque los Ciudadanos no pueden loguearse.
            btn1.text = "Nuevos Reclamos"
            btn2.text = "Reclamos Cerrados"

            actionBtn1 = InicioDirections.actionInicioToReclamoListFragment("Asignado", "")
            actionBtn2 = InicioDirections.actionInicioToReclamoListFragment("Cerrado", "")
        }

        btn1.setOnClickListener{
            v.findNavController().navigate(actionBtn1)
        }

        btn2.setOnClickListener{
            v.findNavController().navigate(actionBtn2)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usuarioViewModel = ViewModelProvider(requireActivity()).get(UsuarioViewModel::class.java)
        if(usuarioViewModel.obtenerUsuarioLogueado()==null){
            var action = InicioDirections.actionInicioToLogIn()
            v.findNavController().navigate(action)
        }
    }

}