package com.udacity.shoestore.screens.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
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
        binding = inflate(inflater, R.layout.welcome_fragment, container, false)
        return binding.root
    }

    /*
     * Assignment on line 35, 36 and 37, is written in form of kotlin expression
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(this).get(WelcomeViewModel::class.java).also { viewModel = it }
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