package com.dteam.ministerio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dteam.ministerio.R

class RegistroExitoso : Fragment() {

    companion object {
        fun newInstance() = RegistroExitoso()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.registro_exitoso_fragment, container, false)
    }
}