package com.udacity.shoestore.screens.ui.shoelist.itemdetail

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.ListShoesViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ItemDetailFragmentBinding
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailFragmentDirections.Companion.actionItemDetailFragmentToScrollingFragment

/**
 * A fragment representing a list of Items.
 */
class ItemDetailFragment : Fragment() {
    private lateinit var binding: ItemDetailFragmentBinding
    private lateinit var viewModel: ItemDetailViewModel
    private val sharedViewModel: ListShoesViewModel by activityViewModels()
    private val afterTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            execute(binding)
        }
    }
    private val closeKeyboard: (TextView?) -> Unit = { textView ->
        textView?.let {
            val imm = it.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, R.layout.item_detail_fragment, container, false)
        ViewModelProvider(this).get(ItemDetailViewModel::class.java).also { viewModel = it }
        this.also { binding.lifecycleOwner = it }
        viewModel.also { binding.itemDetailViewModel = it }
        return binding.root
    }

    /*
     * Assignment on line 37, 38, 39, 40 and 43, is written in form of kotlin expression
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterTextChangeListener.let {
            binding.shoeName.addTextChangedListener(it)
            binding.shoeCompany.addTextChangedListener(it)
            binding.shoeSize.addTextChangedListener(it)
            binding.shoeDescription.addTextChangedListener(it)
        }
        binding.shoeName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_NEXT) {
                false.also { binding.save.isEnabled = it }
            }
            false
        }

        binding.shoeDescription.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == IME_ACTION_DONE) {
                true.also { binding.save.isEnabled = it }
                closeKeyboard(v)
            }
            false
        }

        viewModel.showItemList.observe(viewLifecycleOwner, { enabled ->
            if (enabled) {
                val shoeWithError = viewModel.shoe.value
                shoeWithError?.let { sharedViewModel.setShoeList(it) }
                findNavController().navigate(actionItemDetailFragmentToScrollingFragment())
                viewModel.onShoeItemListComplete()
            }
        })
        viewModel.cancel.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(actionItemDetailFragmentToScrollingFragment())
                viewModel.onCancelItemComplete()
            }
        })
    }

    private fun execute(binding: ItemDetailFragmentBinding) {
        viewModel.onNext(binding.shoeCompany.text.toString(), binding.shoeDescription.text.toString(),
                binding.shoeName.text.toString(), binding.shoeSize.text.toString())
    }
}