package com.udacity.shoestore.screens.ui.shoelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.ListShoesViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ScrollingFragmentBinding
import com.udacity.shoestore.databinding.TextviewRowBinding
import com.udacity.shoestore.databinding.TextviewRowBinding.inflate
import com.udacity.shoestore.models.ShoeWithError
import com.udacity.shoestore.models.Validator.Companion.ShErr
import com.udacity.shoestore.models.Validator.Companion.ShoeError
import com.udacity.shoestore.models.Validator.Companion.ShoeError.*
import com.udacity.shoestore.screens.ui.shoelist.ScrollingFragmentDirections.Companion.actionScrollingFragmentToItemDetailFragment

class ScrollingFragment : Fragment() {
    private lateinit var viewModel: ScrollingFragmentViewModel
    private lateinit var binding: ScrollingFragmentBinding
    private lateinit var shoeRows: LinearLayoutCompat
    private val sharedViewModel: ListShoesViewModel by activityViewModels()
    private fun execute(shoe: ShoeWithError, se: ShoeError) = when (se) {
        is CheckName -> {
            addView().apply {
                id = R.id.reservedShoeName
                text = shoe.name
            }
        }
        is CheckSize -> {
            addView().apply {
                id = R.id.reservedShoeSize
                text = shoe.size.toString()
            }
        }
        is CheckCompany -> {
            addView().apply {
                id = R.id.reservedShoeCompany
                text = shoe.company
            }
        }
        is CheckDescription -> {
            addView().apply {
                id = R.id.reservedShoeDescription
                text = shoe.description
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, R.layout.scrolling_fragment, container, false)
        ViewModelProvider(this).get(ScrollingFragmentViewModel::class.java).also {
            viewModel = it }
        this.also { binding.lifecycleOwner = it }
        viewModel.also { binding.scrollingViewModel = it }
        binding.shoeRows.also { shoeRows = it }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.shoeList.observe(viewLifecycleOwner, { shoeList ->
            for (shoe in shoeList) {
                val checkErrors: ShErr
                (ShErr() + CheckName(shoe) + CheckSize(shoe) + CheckCompany(shoe) +
                        CheckDescription(shoe)).also { checkErrors = it }
                runShoe(shoe, checkErrors)
            }
        })

        viewModel.hasSelectedItemDetail.observe(viewLifecycleOwner, { isItemDetail ->
            if (isItemDetail) {
                findNavController().navigate(actionScrollingFragmentToItemDetailFragment())
                viewModel.selectionItemDetailComplete()
            }
        })
    }

    private fun addView(): TextView {
        var rowBinding: TextviewRowBinding
        inflate(layoutInflater, shoeRows, true).also { rowBinding = it }
        return rowBinding.root
    }

    private fun runShoe(shoeWithError: ShoeWithError, sheErr: ShErr) {
        sheErr.shErrOps.forEach { execute(shoeWithError, it) }
    }
}