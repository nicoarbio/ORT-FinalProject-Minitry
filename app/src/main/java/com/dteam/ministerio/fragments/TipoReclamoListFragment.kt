package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.CategoriaReclamoAdapter
import com.dteam.ministerio.viewmodels.CategoriaViewModel
import com.dteam.ministerio.viewmodels.ReclamoViewModel
import com.google.android.material.snackbar.Snackbar

class TipoReclamoListFragment : Fragment() {

    companion object {
        fun newInstance() = TipoReclamoListFragment()
    }

    private lateinit var categoriaViewModel: CategoriaViewModel
    private lateinit var reclamoViewModel: ReclamoViewModel
    private lateinit var v : View
    private lateinit var listadoCategorias: RecyclerView
    private lateinit var categoriasAdapter: CategoriaReclamoAdapter

    private lateinit var progresBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.tipo_reclamo_list_fragment, container, false)
        listadoCategorias = v.findViewById(R.id.listadoCategorias)
        progresBar = v.findViewById(R.id.progressBarTipoReclamo)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoriaViewModel = ViewModelProvider(requireActivity()).get(CategoriaViewModel::class.java)
        reclamoViewModel = ViewModelProvider(requireActivity()).get(ReclamoViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        listadoCategorias.setHasFixedSize(true)
        listadoCategorias.layoutManager = GridLayoutManager(context, 3)
        categoriaViewModel.getCategorias()
        categoriasAdapter = CategoriaReclamoAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}
        setObserver()
    }

    override fun onResume() {
        super.onResume()
        progresBar.visibility = View.VISIBLE
    }

    fun setObserver(){
        categoriaViewModel.listadoCategorias.observe(viewLifecycleOwner, Observer { list ->
            categoriasAdapter = CategoriaReclamoAdapter(list, requireContext()) { pos -> onItemClick(pos) }
            listadoCategorias.adapter = categoriasAdapter
            progresBar.visibility = View.INVISIBLE
        })

    }

    fun onItemClick(pos: Int){
        val categoria = categoriaViewModel.listadoCategorias.value?.get(pos)
        if (categoria != null) {
            categoriaViewModel.setDocumentId(categoria.documentId!!)
            val action = TipoReclamoListFragmentDirections.actionTipoReclamoListFragmentToSubcategoriaReclamoList(categoria.nombre)
            v.findNavController().navigate(action)
        }else{
            Snackbar.make(v,"Ocurrió un error. Vuelva a intentar mas tarde", Snackbar.LENGTH_SHORT).show()
        }

    }
}
