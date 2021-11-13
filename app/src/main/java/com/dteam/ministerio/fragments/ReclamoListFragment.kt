package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.dteam.ministerio.viewmodels.ReclamoListViewModel
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
        val estadoReclamo  = ReclamoListFragmentArgs.fromBundle(requireArguments()).estadoReclamo
        val subcateg  = ReclamoListFragmentArgs.fromBundle(requireArguments()).subcategoria
        //if (usuarioViewModel.getRol()=="Admin"){
        //TODO get rol
        if ("Admin"=="Admin"){
            if(subcateg == ""){
                reclamoViewModel.getReclamosPorEstado(estadoReclamo, null)
            }else{
                reclamoViewModel.getReclamosPorCateg(subcateg)
            }
        }else
        {
            reclamoViewModel.getReclamosPorEstado(estadoReclamo, usuarioViewModel.obtenerUsuarioLogueado()!!.uid)
        }


        reclamoAdapter = ReclamoAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}
        setObserver()
    }
    fun setObserver(){
        reclamoViewModel.listadoReclamos.observe(viewLifecycleOwner, Observer { list ->
            reclamoAdapter = ReclamoAdapter(list, requireContext()) { pos -> onItemClick(pos) }
            listadoReclamos.adapter = reclamoAdapter
        })
    }

    fun onItemClick(pos: Int){
        val reclamo = reclamoViewModel.listadoReclamos.value?.get(pos)
        reclamoViewModel.reclamo.value = reclamo
        val actionToDetalle = ReclamoListFragmentDirections.actionReclamoListFragmentToDetalleReclamoAdmin()
        v.findNavController().navigate(actionToDetalle)
    }

}