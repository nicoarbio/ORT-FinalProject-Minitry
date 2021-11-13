package com.dteam.ministerio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dteam.ministerio.R

class ResponsablePerfil : Fragment() {

    companion object {
        fun newInstance() = ResponsablePerfil()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.responsable_perfil_fragment, container, false)
    }
}