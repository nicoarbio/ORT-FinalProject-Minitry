package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dteam.ministerio.R
import com.dteam.ministerio.viewmodels.ResponsablePerfilViewModel

class ResponsablePerfil : Fragment() {

    companion object {
        fun newInstance() = ResponsablePerfil()
    }

    private lateinit var viewModel: ResponsablePerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.responsable_perfil_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResponsablePerfilViewModel::class.java)
        // TODO: Use the ViewModel
    }

}