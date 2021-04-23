package com.udacity.shoestore.screens.ui.instructions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InstructionsViewModel : ViewModel() {
    private var _selectedList = MutableLiveData<Boolean>()
    val hasSelectedList: LiveData<Boolean>
        get() = _selectedList

    init{
         hasCompletedList()
    }
    fun onSelectedList() {
        true.also { _selectedList.value = it }
    }
    fun hasCompletedList() {
        false.also { _selectedList.value = it }
    }
}