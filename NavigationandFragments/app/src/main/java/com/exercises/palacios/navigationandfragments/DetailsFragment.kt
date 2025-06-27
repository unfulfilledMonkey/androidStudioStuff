package com.exercises.palacios.navigationandfragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private lateinit var viewModel: MyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        view.findViewById<TextView>(R.id.sharedText).text = viewModel.message

        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}