package com.udacity.shoestore.screens.ui.shoelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScrollingFragmentViewModel : ViewModel() {

    private var _selectedItemDetail = MutableLiveData<Boolean>()
    val hasSelectedItemDetail: LiveData<Boolean>
        get() = _selectedItemDetail
    init {
        selectionItemDetailComplete()
    }

    fun onSelectedItemDetail() {
        true.also { _selectedItemDetail.value = it }
    }

    fun selectionItemDetailComplete() {
        false.also { _selectedItemDetail.value = it }
    }
}