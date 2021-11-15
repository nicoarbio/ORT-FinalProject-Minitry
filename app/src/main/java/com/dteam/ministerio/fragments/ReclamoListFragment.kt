package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ReclamoAdapter
import com.dteam.ministerio.viewmodels.ReclamoViewModel
import com.dteam.ministerio.viewmodels.UsuarioViewModel

class ReclamoListFragment : Fragment() {

    companion object {
        fun newInstance() = ReclamoListFragment()
    }

    private lateinit var reclamoViewModel: ReclamoViewModel
    private lateinit var usuarioViewModel: UsuarioViewModel

    private lateinit var v: View

    private lateinit var listadoReclamos: RecyclerView
    private lateinit var reclamoAdapter: ReclamoAdapter

    var estadoReclamo : String? = null
    var subcateg : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.reclamo_list_fragment, container, false)
        listadoReclamos = v.findViewById(R.id.listadoReclamos)
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

        estadoReclamo  = ReclamoListFragmentArgs.fromBundle(requireArguments()).estadoReclamo
        subcateg  = ReclamoListFragmentArgs.fromBundle(requireArguments()).subcategoria

        reclamoAdapter = ReclamoAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}

        setObserver()

        usuarioViewModel.actualizarUsuarioRolLogueado()
    }

    fun setObserver(){
        reclamoViewModel.listadoReclamos.observe(viewLifecycleOwner, Observer { list ->
            if (list.size == 0) {
                //TODO mostrarMensajeError()
                Log.d("Test","No trajo ningun reclamo")
            } else {
                reclamoAdapter = ReclamoAdapter(list, requireContext()) { pos -> onItemClick(pos) }
                listadoReclamos.adapter = reclamoAdapter
            }
        })

        usuarioViewModel.usuarioRol.observe(viewLifecycleOwner, Observer { rol ->
            when(rol) {
                "Admin" -> {
                    usuarioViewModel.filtrarReclamos(null, estadoReclamo, subcateg)
                }
                "Responsable" -> {
                    usuarioViewModel.filtrarReclamos(usuarioViewModel.obtenerUsuarioLogueado()!!.uid, estadoReclamo, subcateg)
                }
            }
        })
    }

    fun onItemClick(pos: Int){
        val reclamo = reclamoViewModel.listadoReclamos.value?.get(pos)
        reclamoViewModel.reclamo.value = reclamo
        val actionToDetalle = ReclamoListFragmentDirections.actionReclamoListFragmentToDetalleReclamoAdmin()
        v.findNavController().navigate(actionToDetalle)
    }

}