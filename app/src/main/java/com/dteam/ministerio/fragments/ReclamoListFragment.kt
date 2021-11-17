package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ReclamoAdapter
import com.dteam.ministerio.viewmodels.ReclamoViewModel
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import java.lang.Exception

class ReclamoListFragment : Fragment() {

    companion object {
        fun newInstance() = ReclamoListFragment()
    }

    private lateinit var reclamoViewModel: ReclamoViewModel
    private lateinit var usuarioViewModel: UsuarioViewModel

    private lateinit var v: View

    private lateinit var lblNoItems: TextView

    private lateinit var listadoReclamos: RecyclerView
    private lateinit var reclamoAdapter: ReclamoAdapter

    var estadoReclamo : String? = null
    var subcateg : String? = null

    private lateinit var progresBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.reclamo_list_fragment, container, false)
        listadoReclamos = v.findViewById(R.id.listadoReclamos)
        progresBar = v.findViewById(R.id.progressBarListaReclamo)
        lblNoItems = v.findViewById(R.id.lblNoItems)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reclamoViewModel = ViewModelProvider(requireActivity()).get(ReclamoViewModel::class.java)
        usuarioViewModel = ViewModelProvider(requireActivity()).get(UsuarioViewModel::class.java)
        if(usuarioViewModel.obtenerUsuarioLogueado()==null){
            var action = ReclamoListFragmentDirections.actionReclamoListFragmentToLogIn()
            v.findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        listadoReclamos.setHasFixedSize(true)
        listadoReclamos.layoutManager = LinearLayoutManager(context)

        lblNoItems.visibility = View.GONE

        try {
            estadoReclamo  = ReclamoListFragmentArgs.fromBundle(requireArguments()).estadoReclamo
        } catch (e:Exception) {
            estadoReclamo = null
        }
        try {
            subcateg  = ReclamoListFragmentArgs.fromBundle(requireArguments()).subcategoria
        } catch (e:Exception) {
            subcateg = null
        }

        reclamoAdapter = ReclamoAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}
        setObserver()

        reclamoViewModel.getReclamos()
        reclamoViewModel.listadoReclamos.observe(viewLifecycleOwner, Observer { list ->
            usuarioViewModel.actualizarUsuarioRolLogueado()
        })
    }

    override fun onResume() {
        super.onResume()
        progresBar.visibility = View.VISIBLE
        lblNoItems.visibility = View.INVISIBLE
    }

    fun setObserver(){
        reclamoViewModel.reclamosFiltrados.observe(viewLifecycleOwner, Observer { list ->
            progresBar.visibility = View.INVISIBLE
            if (list.size == 0) {
                lblNoItems.visibility = View.VISIBLE
                Log.d("Test","No trajo ningun reclamo")
            } else {
                lblNoItems.visibility = View.GONE
                reclamoAdapter = ReclamoAdapter(list, requireContext()) { pos -> onItemClick(pos) }
                listadoReclamos.adapter = reclamoAdapter
            }
        })

        usuarioViewModel.usuarioRol.observe(viewLifecycleOwner, Observer { rol ->
            when(rol) {
                "Admin" -> {
                    reclamoViewModel.filtrarReclamos(null, estadoReclamo, subcateg)
                }
                "Responsable" -> {
                    reclamoViewModel.filtrarReclamos(usuarioViewModel.obtenerUsuarioLogueado()!!.uid, estadoReclamo, subcateg)
                }
            }
        })
    }

    fun onItemClick(pos: Int){
        val reclamo = reclamoViewModel.reclamosFiltrados.value?.get(pos)
        reclamoViewModel.reclamo.value = reclamo
        val actionToDetalle = ReclamoListFragmentDirections.actionReclamoListFragmentToDetalleReclamoAdmin()
        v.findNavController().navigate(actionToDetalle)
    }

}