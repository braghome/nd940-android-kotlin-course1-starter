package com.udacity.shoestore.screens.ui.shoelist.itemdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ItemDetailFragmentBinding
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailFragmentDirections.Companion.actionItemDetailFragmentToScrollingFragment

/**
 * A fragment representing a list of Items.
 */
class ItemDetailFragment : Fragment() {
    private lateinit var binding: ItemDetailFragmentBinding
    private lateinit var viewModel: ItemDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_detail_fragment, container,
            false)
        return binding.root
    }

    /*
     * Assignment on line 37, 38, 39, 40 and 43, is written in form of kotlin expression
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(this).get(ItemDetailViewModel::class.java).also { viewModel = it }
        this.also { binding.lifecycleOwner = it }
        viewModel.also { binding.itemDetailViewModel = it }
        viewModel.onShowList().also { binding.save.isEnabled = it }

        viewModel.showItemList.observe(viewLifecycleOwner, { fieldsPresent ->
            viewModel.onShowList().also { binding.save.isEnabled = it }
            if (fieldsPresent) {
                viewModel.onSave()
                findNavController().navigate(actionItemDetailFragmentToScrollingFragment())
                viewModel.onItemDetailComplete()
            }
        })
    }
}