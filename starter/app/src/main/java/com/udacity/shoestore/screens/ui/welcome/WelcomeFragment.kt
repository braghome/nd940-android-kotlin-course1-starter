package com.udacity.shoestore.screens.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.WelcomeFragmentBinding
import com.udacity.shoestore.screens.ui.welcome.WelcomeFragmentDirections.Companion.actionWelcomeFragmentToInstructionsFragment

class WelcomeFragment : Fragment() {

    private lateinit var viewModel: WelcomeViewModel
    private lateinit var binding: WelcomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.welcome_fragment, container,
                false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        this.also { binding.lifecycleOwner = it }
        viewModel.also { binding.welcomeViewModel = it }
        viewModel.hasSelectedInstructions.observe(viewLifecycleOwner, { instructions ->
            if (instructions) {
                findNavController().navigate(actionWelcomeFragmentToInstructionsFragment())
                viewModel.onInstructionsComplete()
            }
        })
    }
}