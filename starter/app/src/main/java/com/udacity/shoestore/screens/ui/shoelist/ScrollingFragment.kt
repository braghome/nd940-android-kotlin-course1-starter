package com.udacity.shoestore.screens.ui.shoelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.ListShoesViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ScrollingFragmentBinding
import com.udacity.shoestore.databinding.ScrollingRowBinding
import com.udacity.shoestore.databinding.ScrollingRowBinding.*
import com.udacity.shoestore.screens.ui.shoelist.ScrollingFragmentDirections.Companion.actionScrollingFragmentToItemDetailFragment
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel

class ScrollingFragment : Fragment() {
    private lateinit var viewModel: ScrollingFragmentViewModel
    private lateinit var binding: ScrollingFragmentBinding
    private val sharedViewModel: ListShoesViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, R.layout.scrolling_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(this).get(ScrollingFragmentViewModel::class.java).also {
            viewModel = it }
        this.also { binding.lifecycleOwner = it }
        viewModel.also { binding.scrollingViewModel = it }
        val shoeList: LinearLayoutCompat
        binding.shoeList.also { shoeList = it }
        val rowBinding: ScrollingRowBinding
        inflate(layoutInflater).also { rowBinding = it }
        rowBinding.shoeName

        viewModel.hasSelectedItemDetail.observe(viewLifecycleOwner, { isItemDetail ->
            if (isItemDetail) {
                findNavController().navigate(actionScrollingFragmentToItemDetailFragment())
                viewModel.selectionItemDetailComplete()
            }
        })
    }
}