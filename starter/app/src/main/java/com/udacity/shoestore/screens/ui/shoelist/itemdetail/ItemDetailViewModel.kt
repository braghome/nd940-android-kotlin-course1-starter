package com.udacity.shoestore.screens.ui.shoelist.itemdetail

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.models.Validator
import com.udacity.shoestore.models.Validator.Companion.SHOE_DETAILS_ERROR
import com.udacity.shoestore.models.Validator.Companion.ShErr
import com.udacity.shoestore.models.Validator.Companion.ShoeError.*
import com.udacity.shoestore.models.Validator.Companion.predicateList
import com.udacity.shoestore.models.Validator.Companion.runShoe
import com.udacity.shoestore.models.Validator.Companion.verify
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel.ShoeState.InValid
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel.ShoeState.Valid
import timber.log.Timber

class ItemDetailViewModel : ViewModel() {
    private var _shoe = MutableLiveData<Shoe>()

    var shoeName = MutableLiveData<String>()

    var shoeCompany = MutableLiveData<String>()

    val shoeSize = ObservableInt()

    val description = MutableLiveData<String>()
    private var _cancel = MutableLiveData<Boolean>()
    val cancel: LiveData<Boolean>
        get() = _cancel
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
        onCancelItemComplete()
        val shoe: Shoe
        Shoe(shoeName.value ?: "", shoeSize.get(), shoeCompany.value ?: "",
                description.value ?: "").also { shoe = it }
        shoe.also { _shoe.value = it }
    }

    fun onCancelItemComplete() {
        false.also { _cancel.value = it }
    }

    fun onCancel() {
        true.also { _cancel.value = it }
    }

    fun onItemDetailComplete() {
        false.also { _showItemList.value = it }
    }

    fun onItemDetail() {
        val shoe = _shoe.value
        shoe?.let {
            it.copy(noError = true).also { _shoe.value = it }
        }
    }

    fun onNext(company: String, description: String, name: String, size: String) {
        _shoe.value?.let { innerShoe ->
            innerShoe.copy(company = company).also { _shoe.value = it }
            innerShoe.copy(description = description).also { _shoe.value = it }
            innerShoe.copy(name = name).also { _shoe.value = it }
            innerShoe.copy(size = Integer.valueOf(size)).also { _shoe.value = it }
            val checkErrors: ShErr
            (ShErr() + CheckName(innerShoe) + CheckSize(innerShoe) + CheckCompany(innerShoe) +
                    CheckDescription(innerShoe)).also { checkErrors = it }
            runShoe(innerShoe, checkErrors)
        }
    }

    fun onShowList(): Boolean {
        val shoe = _shoe.value
        if (null != shoe) {
            Timber.i("the shoe state is:- $shoe")
            _showItemList.value = shoe.noError
        }
        return _showItemList.value ?: false
    }

    enum class ShoeState {
        Valid,
        InValid
    }
}
