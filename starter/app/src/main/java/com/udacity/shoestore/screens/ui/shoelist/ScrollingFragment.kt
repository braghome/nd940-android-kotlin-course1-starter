package com.udacity.shoestore.screens.ui.shoelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ScrollingFragmentBinding
import com.udacity.shoestore.screens.ui.shoelist.ScrollingFragmentDirections.Companion.actionScrollingFragmentToItemDetailFragment

class ScrollingFragment : Fragment() {
    private lateinit var viewModel: ScrollingFragmentViewModel
    private lateinit var binding: ScrollingFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<ScrollingFragmentBinding>(inflater, R.layout.scrolling_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScrollingFragmentViewModel::class.java)
        this.also { binding.lifecycleOwner = it }
        binding.scrollingViewModel = viewModel
        viewModel.hasSelectedItemDetail.observe(viewLifecycleOwner, { isItemDetail ->
            if (isItemDetail) {
                findNavController().navigate(actionScrollingFragmentToItemDetailFragment())
                viewModel.selectionItemDetailComplete()
            }
        })
    }
}