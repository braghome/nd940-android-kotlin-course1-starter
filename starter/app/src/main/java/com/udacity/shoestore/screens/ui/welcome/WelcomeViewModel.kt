package com.udacity.shoestore.screens.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {
    private var _selectedInstructions= MutableLiveData<Boolean>()
    val hasSelectedInstructions: LiveData<Boolean>
        get() = _selectedInstructions
    init {
        onInstructionsComplete()
    }
    fun onSelectInstructions() {
        true.also { _selectedInstructions.value = it }
    }
    fun onInstructionsComplete() {
        false.also { _selectedInstructions.value = it }
    }
}