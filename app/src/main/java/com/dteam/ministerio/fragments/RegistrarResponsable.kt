package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dteam.ministerio.R
import com.dteam.ministerio.viewmodels.RegistrarResponsableViewModel

class RegistrarResponsable : Fragment() {

    companion object {
        fun newInstance() = RegistrarResponsable()
    }

    private lateinit var viewModel: RegistrarResponsableViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.registrar_responsable_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrarResponsableViewModel::class.java)
        // TODO: Use the ViewModel
    }

}