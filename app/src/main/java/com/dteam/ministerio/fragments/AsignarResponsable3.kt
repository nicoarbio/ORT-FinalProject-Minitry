package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dteam.ministerio.R
import com.dteam.ministerio.viewmodels.AsignarResponsable3ViewModel

class AsignarResponsable3 : Fragment() {

    companion object {
        fun newInstance() = AsignarResponsable3()
    }

    private lateinit var viewModel: AsignarResponsable3ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.asignar_responsable3_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AsignarResponsable3ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}