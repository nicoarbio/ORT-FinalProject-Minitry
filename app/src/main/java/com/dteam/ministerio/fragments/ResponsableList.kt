package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ListaResponsableAdapter
import com.dteam.ministerio.entities.Usuario
import com.google.android.material.snackbar.Snackbar

class ResponsableList : Fragment() {

    companion object {
        fun newInstance() = ResponsableList()
    }

    //private lateinit var usuarioViewModel: UsuarioViewModel

    private lateinit var v: View

    private lateinit var listadoResponsable: RecyclerView
    private lateinit var responsableAdapter: ListaResponsableAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.responsable_list_fragment, container, false)
        listadoResponsable = v.findViewById(R.id.recResponsable)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ResponsableListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        listadoResponsable.setHasFixedSize(true)
        listadoResponsable.layoutManager = LinearLayoutManager(context)
        //usuarioViewModel.getReclamos()
        var listaUsuarioPrueba = mutableListOf<Usuario>()
        listaUsuarioPrueba.add(Usuario("","","","","Pepe","Botella","99.999.999","","",""))
        listaUsuarioPrueba.add(Usuario("","","","","Pepe","Botella","99.999.999","","",""))
        listaUsuarioPrueba.add(Usuario("","","","","Pepe","Botella","99.999.999","","",""))
        listaUsuarioPrueba.add(Usuario("","","","","Pepe","Botella","99.999.999","","",""))
        listaUsuarioPrueba.add(Usuario("","","","","Pepe","Botella","99.999.999","","",""))
        listadoResponsable.adapter = ListaResponsableAdapter(listaUsuarioPrueba, requireContext()) { pos -> onItemClick(pos)}
        setObserver()
    }

    fun setObserver(){
       /* reclamoViewModel.listadoReclamos.observe(viewLifecycleOwner, Observer { list ->
            reclamoAdapter = ReclamoAdapter(list, requireContext()) { pos -> onItemClick(pos) }
            listadoReclamos.adapter = reclamoAdapter
        })*/
    }

    fun onItemClick(pos: Int){
        //val reclamo = reclamoViewModel.listadoReclamos.value?.get(pos)
        //reclamoViewModel.reclamo.value = reclamo
        Snackbar.make(v,"Responsable nro:" + pos, Snackbar.LENGTH_SHORT).show()
        val actionToDetalle = ResponsableListDirections.actionResponsableListToDetalleReclamoAdmin()
        v.findNavController().navigate(actionToDetalle)
    }

}