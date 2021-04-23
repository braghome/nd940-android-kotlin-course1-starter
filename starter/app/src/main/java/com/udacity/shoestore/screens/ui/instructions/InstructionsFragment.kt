package com.udacity.shoestore.screens.ui.instructions

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
import com.udacity.shoestore.databinding.InstructionsFragmentBinding
import com.udacity.shoestore.screens.ui.instructions.InstructionsFragmentDirections.Companion.actionInstructionsFragmentToScrollingFragment

class InstructionsFragment : Fragment() {

    companion object {
        fun newInstance() = InstructionsFragment()
    }

    private lateinit var viewModel: InstructionsViewModel
    private lateinit var binding: InstructionsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<InstructionsFragmentBinding>(inflater, R.layout.instructions_fragment,
                container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InstructionsViewModel::class.java)
        this.also { binding.lifecycleOwner = it }
        binding.instructionVewModel = viewModel
        viewModel.hasSelectedList.observe(viewLifecycleOwner, Observer { hasClickedInstructions ->
            if (hasClickedInstructions) {
                findNavController().navigate(actionInstructionsFragmentToScrollingFragment())
                viewModel.hasCompletedList()
            }
        })
    }

}