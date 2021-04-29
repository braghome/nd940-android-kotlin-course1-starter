package com.udacity.shoestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.ShoeWithError
import timber.log.Timber
import java.util.*
import java.util.Collections.emptyList
import kotlin.collections.ArrayList

class ListShoesViewModel : ViewModel() {
    private var _shoeList = MutableLiveData<MutableList<ShoeWithError>>()
    val shoeList: LiveData<MutableList<ShoeWithError>>
        get() = _shoeList

    fun setShoeList(shoe: ShoeWithError) {
        if (null == _shoeList.value) {
            _shoeList.value = ArrayList()
            _shoeList.value?.let {
                it.add(shoe)
                Timber.i("has shoeList status of $it")
            }
        } else {
            _shoeList.value?.add(shoe)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _shoeList.value = emptyList()
    }
}