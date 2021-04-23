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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = DataBindingUtil.inflate<WelcomeFragmentBinding>(inflater, R.layout.welcome_fragment,
                container, false)
        this.also { bind.lifecycleOwner = it }
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        bind.welcomeViewModel = viewModel
        viewModel.hasSelectedInstructions.observe(viewLifecycleOwner, { instructions ->
            if (instructions) {
                findNavController().navigate(actionWelcomeFragmentToInstructionsFragment())
                viewModel.onInstructionsComplete()
            }
        })

        return bind.root
    }

}