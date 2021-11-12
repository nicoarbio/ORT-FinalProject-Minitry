package com.dteam.ministerio.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.dteam.ministerio.R


class GestionarReclamos : Fragment() {

    companion object {
        fun newInstance() = GestionarReclamos()
    }


    lateinit var v: View
    private lateinit var nuevosReclamos: Button
    private lateinit var reclamosPorCateg: Button
    private lateinit var reclamosAsignados: Button
    private lateinit var reclamosCerrados: Button
    private lateinit var reclamosCancelados: Button
    private var subcateg: String = ""
    private var estadoReclamo = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.gestionar_reclamos_fragment, container, false)
        nuevosReclamos = v.findViewById(R.id.btnNuevosReclamos)
        nuevosReclamos.setOnClickListener{
            estadoReclamo = "Abierto"
            val action = GestionarReclamosDirections.actionGestionarReclamosToReclamoListFragment(estadoReclamo,subcateg)
            v.findNavController().navigate(action)

        }


        reclamosPorCateg = v.findViewById(R.id.btnReclamosPorCategoria)
        reclamosPorCateg.setOnClickListener{
            val action2 = GestionarReclamosDirections.actionGestionarReclamosToTipoReclamoListFragment()
            v.findNavController().navigate(action2)

        }

        reclamosAsignados = v.findViewById(R.id.btnReclamosAsignados)
        reclamosAsignados.setOnClickListener{
            estadoReclamo = "Asignado"
            val action3 = GestionarReclamosDirections.actionGestionarReclamosToReclamoListFragment(estadoReclamo,subcateg)
            v.findNavController().navigate(action3)

        }
        reclamosCerrados = v.findViewById(R.id.btnReclamosCerrados)
        reclamosCerrados.setOnClickListener{
            estadoReclamo = "Cerrado"
            val action4 = GestionarReclamosDirections.actionGestionarReclamosToReclamoListFragment(estadoReclamo,subcateg)
            v.findNavController().navigate(action4)

        }
        reclamosCancelados = v.findViewById(R.id.btnReclamosCancelados)
        reclamosCancelados.setOnClickListener{
            estadoReclamo = "Cancelado"
            val action5 = GestionarReclamosDirections.actionGestionarReclamosToReclamoListFragment(estadoReclamo,subcateg)
            v.findNavController().navigate(action5)

        }

        return v
    }


}

