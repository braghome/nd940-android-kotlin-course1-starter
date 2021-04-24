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
        binding = DataBindingUtil.inflate(inflater, R.layout.scrolling_fragment, container,
                false)
        return binding.root
    }

    /*
     * Assignment on line 34, 36 and 37, is written in form of kotlin expression
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(this).get(ScrollingFragmentViewModel::class.java).also {
            viewModel = it }
        this.also { binding.lifecycleOwner = it }
        viewModel.also { binding.scrollingViewModel = it }
        viewModel.hasSelectedItemDetail.observe(viewLifecycleOwner, { isItemDetail ->
            if (isItemDetail) {
                findNavController().navigate(actionScrollingFragmentToItemDetailFragment())
                viewModel.selectionItemDetailComplete()
            }
        })
    }
}