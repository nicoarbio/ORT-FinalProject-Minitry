package com.dteam.ministerio.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dteam.ministerio.R
import com.dteam.ministerio.viewmodels.DetalleReclamoViewModel

class DetalleReclamoAdmin : Fragment() {

    companion object {
        fun newInstance() = DetalleReclamoAdmin()
    }

    private lateinit var viewModel: DetalleReclamoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detalle_reclamo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetalleReclamoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}