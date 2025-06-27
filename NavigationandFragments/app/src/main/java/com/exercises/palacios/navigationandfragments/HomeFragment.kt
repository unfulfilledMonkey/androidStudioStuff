package com.exercises.palacios.navigationandfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewModel: MyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        view.findViewById<View>(R.id.navigateButton).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_details)
        }
    }
}