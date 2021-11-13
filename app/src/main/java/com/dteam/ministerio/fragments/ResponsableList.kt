package com.dteam.ministerio.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dteam.ministerio.R
import com.dteam.ministerio.adapters.ListaResponsableAdapter
import com.dteam.ministerio.entities.Usuario
import com.dteam.ministerio.viewmodels.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast

import com.dteam.ministerio.activities.MainActivity
import com.dteam.ministerio.viewmodels.ReclamoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ResponsableList : Fragment() {

    companion object {
        fun newInstance() = ResponsableList()
    }

    private lateinit var usuarioViewModel: UsuarioViewModel

    private lateinit var v: View

    private lateinit var listadoResponsable: RecyclerView
    private lateinit var responsableAdapter: ListaResponsableAdapter

    private lateinit var searchResponList: SearchView

    var listaFiltrada = mutableListOf<Usuario>()

    private lateinit var accion: String
    private lateinit var btnAgregarRespon: FloatingActionButton
    private lateinit var reclamoViewModel: ReclamoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.responsable_list_fragment, container, false)
        listadoResponsable = v.findViewById(R.id.recResponsable)
        searchResponList = v.findViewById(R.id.searchResponList)
        btnAgregarRespon = v.findViewById(R.id.btnAgregarRespon)

        searchResponList.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                usuarioViewModel.getUsuariosResponsables()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if(newText.isEmpty()){
                        usuarioViewModel.getUsuariosResponsables()
                    }
                }
                return false
            }

        })

        btnAgregarRespon.setOnClickListener {
            val actionToAddRespon = ResponsableListDirections.actionResponsableListToRegistrarResponsable()
            v.findNavController().navigate(actionToAddRespon)
        }

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
        reclamoViewModel = ViewModelProvider(this).get(ReclamoViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        accion = ResponsableListArgs.fromBundle(requireArguments()).accion
        listadoResponsable.setHasFixedSize(true)
        listadoResponsable.layoutManager = LinearLayoutManager(context)
        usuarioViewModel.getUsuariosResponsables()
        listadoResponsable.adapter = ListaResponsableAdapter(mutableListOf(), requireContext()) { pos -> onItemClick(pos)}
        setObserver()
    }

    fun setObserver(){
        usuarioViewModel.usuariosResponsables.observe(viewLifecycleOwner, Observer { list ->
            var buscado = searchResponList.query
            if(!buscado.isEmpty()){
                buscado = buscado.toString().lowercase()
                listaFiltrada = (list.filter { usuario -> usuario.nombre.lowercase().contains(buscado) || usuario.apellido.lowercase().contains(buscado)}).toMutableList()
            }else{
                listaFiltrada = list
            }
            responsableAdapter = ListaResponsableAdapter(listaFiltrada, requireContext()) { pos -> onItemClick(pos) }
            listadoResponsable.adapter = responsableAdapter
        })
    }

    fun onItemClick(pos: Int){
        val respon = listaFiltrada[pos]
        if(accion == "ASIGNAR"){
            showdialogAsignarRespon(respon)
        } else {
            usuarioViewModel.usuario.value = respon
            // TODO: traer el perfil de usuario de ciudadanos para hacer este action
            //val actionToPerfil = ResponsableListDirections.actionResponsableListTo PerfilResponsable()
            //v.findNavController().navigate(actionToPerfil)
        }
    }

    fun showdialogAsignarRespon(respon: Usuario) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Asignar Responsable")
        builder.setMessage("Esta seguro de asignar a " + respon.nombre + " " + respon.apellido + " a este Reclamo?")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            reclamoViewModel.setResponsable(respon)
            reclamoViewModel.estadoGuardadoOk.observe(viewLifecycleOwner, Observer{list ->
                if(reclamoViewModel.estadoGuardadoOk.value==true){
                    Snackbar.make(v,"Se Asignó correctamente", Snackbar.LENGTH_SHORT).show()
                    val actionToDetalle = ResponsableListDirections.actionResponsableListToDetalleReclamoAdmin()
                    v.findNavController().navigate(actionToDetalle)
                }else{
                    Snackbar.make(v,R.string.errorGeneral, Snackbar.LENGTH_SHORT).show()
                }
            })
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

}