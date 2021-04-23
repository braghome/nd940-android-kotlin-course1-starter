package com.udacity.shoestore.screens.ui.shoelist.itemdetail

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.Validator.Companion.SHOE_DETAILS_ERROR
import com.udacity.shoestore.models.Validator.Companion.ShErr
import com.udacity.shoestore.models.Validator.Companion.ShoeError.*
import com.udacity.shoestore.models.Validator.Companion.predicateList
import com.udacity.shoestore.models.Validator.Companion.runShoe
import com.udacity.shoestore.models.Validator.Companion.verify
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel.ShoeState.InValid
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel.ShoeState.Valid

class ItemDetailViewModel : ViewModel() {
    private var _shoe = MutableLiveData<Shoe>()
    val shoe: LiveData<Shoe>
        get() = _shoe

    val shoeName = MutableLiveData<String>()

    val shoeCompany = MutableLiveData<String>()

    val shoeSize = ObservableInt()

    val description = MutableLiveData<String>()
    private var _showItemList = MutableLiveData<Boolean>()
    val showItemList: LiveData<Boolean>
        get() = _showItemList

    private val isShoeNameValid: LiveData<ShoeState> = map(shoeName) {
        when (verify(it)) {
            true -> InValid
            false -> Valid
        }
    }
    val userNameError: LiveData<String> = map(isShoeNameValid) {
        when (it) {
            Valid -> ""
            InValid -> SHOE_DETAILS_ERROR // It should be get from R.string
        }
    }
    init {
        onItemDetailComplete()
    }

    fun onItemDetailComplete() {
        false.also { _showItemList.value = it }
    }

    fun onSave() {
        val shoe: Shoe
        Shoe(shoeName.value ?: "", shoeSize.get(), shoeCompany.value ?: "",
                description.value ?: "").also { shoe = it }
        val checkErrors: ShErr
        (ShErr() + CheckName(shoe) + CheckSize(shoe) + CheckCompany(shoe) + CheckDescription(shoe)
                ).also { checkErrors = it }
        runShoe(shoe, checkErrors)
        shoe.also { _shoe.value = it }
    }

    fun onShowList(): Boolean {
        val shoe = _shoe.value
        if (null != shoe) {
            _showItemList.value = predicateList.all { it(shoe) }
        }
        return _showItemList.value ?: false
    }

    enum class ShoeState {
        Valid,
        InValid
    }

}
