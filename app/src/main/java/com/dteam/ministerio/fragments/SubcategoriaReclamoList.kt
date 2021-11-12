package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.SubcategoriaReclamoAdapter
import com.dteam.ministerio.viewmodels.CategoriaViewModel
import com.dteam.ministerio.viewmodels.ReclamoViewModel

class SubcategoriaReclamoList : Fragment() {

    companion object {
        fun newInstance() = SubcategoriaReclamoList()
    }

    private lateinit var categoriaViewModel: CategoriaViewModel
    private lateinit var reclamoViewModel: ReclamoViewModel
    private lateinit var v : View
    private lateinit var listadoSubcategorias: RecyclerView
    private lateinit var subCategoriasAdapter: SubcategoriaReclamoAdapter
    private lateinit var categ: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.subcategoria_reclamo_list_fragment, container, false)
        listadoSubcategorias = v.findViewById(R.id.listadoSubcategorias)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoriaViewModel = ViewModelProvider(requireActivity()).get(CategoriaViewModel::class.java)
        reclamoViewModel = ViewModelProvider(requireActivity()).get(ReclamoViewModel::class.java)
    }

    override fun onStart()
    {
        super.onStart()
        listadoSubcategorias.setHasFixedSize(true)
        listadoSubcategorias.layoutManager = LinearLayoutManager(context)
        categ  = SubcategoriaReclamoListArgs.fromBundle(requireArguments()).categoria
        categoriaViewModel.getSubcategorias()
        subCategoriasAdapter = SubcategoriaReclamoAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}
        setObserver()
    }
    fun setObserver(){
        categoriaViewModel.listadoSubcategoria.observe(viewLifecycleOwner, Observer { list ->
            subCategoriasAdapter = SubcategoriaReclamoAdapter(list, requireContext()) { pos -> onItemClick(pos) }
            listadoSubcategorias.adapter = subCategoriasAdapter
        })
    }

    fun onItemClick(pos: Int){
        val subCategoria = categoriaViewModel.listadoSubcategoria.value?.get(pos)?.nombre
        var subcateg:String =  subCategoria.toString()

        val action = SubcategoriaReclamoListDirections.actionSubcategoriaReclamoListToReclamoListFragment("",subcateg)
        v.findNavController().navigate(action)
    }

}