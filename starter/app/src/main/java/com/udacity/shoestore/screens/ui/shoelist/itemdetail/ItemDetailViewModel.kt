package com.udacity.shoestore.screens.ui.shoelist.itemdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.ShoeWithError
import com.udacity.shoestore.models.Validator.Companion.SHOE_DETAILS_ERROR
import com.udacity.shoestore.models.Validator.Companion.ShErr
import com.udacity.shoestore.models.Validator.Companion.ShoeError.*
import com.udacity.shoestore.models.Validator.Companion.runShoe
import com.udacity.shoestore.models.Validator.Companion.verify
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel.ShoeState.InValid
import com.udacity.shoestore.screens.ui.shoelist.itemdetail.ItemDetailViewModel.ShoeState.Valid
import timber.log.Timber

class ItemDetailViewModel : ViewModel() {
    private var _shoe = MutableLiveData<ShoeWithError>()
    val shoe: LiveData<ShoeWithError>
        get() = _shoe
    var shoeName = MutableLiveData<String>()

    var shoeCompany = MutableLiveData<String>()

    val shoeSize = MutableLiveData(0)

    val description = MutableLiveData<String>()
    private var _cancel = MutableLiveData<Boolean>()
    val cancel: LiveData<Boolean>
        get() = _cancel

    private val _showItemList = MediatorLiveData<Boolean>()
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
        onShoeItemListComplete()
        onCancelItemComplete()
        shoeReset()
        _shoe.value?.let { shoe ->
            _showItemList.addSource(shoeName) {shoe.noError}
            _showItemList.addSource(shoeCompany) {shoe.noError}
            _showItemList.addSource(shoeSize) {shoe.noError}
            _showItemList.addSource(description) {shoe.noError}
        }
    }

    private fun shoeReset() {
        val shoeWithError: ShoeWithError
        ShoeWithError(shoeName.value ?: "", shoeSize.value ?: 0,
                shoeCompany.value ?: "", description.value ?: "").also {
            shoeWithError = it
        }
        shoeWithError.also { _shoe.value = it }
    }

    override fun onCleared() {
        super.onCleared()
        _showItemList.removeSource(shoeName)
        _showItemList.removeSource(shoeCompany)
        _showItemList.removeSource(shoeSize)
        _showItemList.removeSource(description)
        shoeReset()
    }

    fun onCancelItemComplete() {
        false.also { _cancel.value = it }
    }

    fun onCancel() {
        true.also { _cancel.value = it }
        shoeReset()
    }

    fun onNext(company: String, description: String, name: String, size: String) {
        _shoe.value?.let { innerShoe ->
            company.also { innerShoe.company = it }
            description.also { innerShoe.description = it }
            name.also { innerShoe.name = it }
            Integer.valueOf(size).also { innerShoe.size = it }
            validateErrorRemoved(innerShoe)
        }
    }

    private fun validateErrorRemoved(innerShoe: ShoeWithError) {
        val checkErrors: ShErr
        (ShErr() + CheckName(innerShoe) + CheckSize(innerShoe) + CheckCompany(innerShoe) +
                CheckDescription(innerShoe)).also { checkErrors = it }
        runShoe(innerShoe, checkErrors)
        Timber.i("$innerShoe has ${innerShoe.noError} shoe state on pre-check")
        innerShoe.also { _shoe.value = it }
    }

    fun onShoeItemList() {
        true.also { _showItemList.value = it }
    }

    fun onShoeItemListComplete() {
        false.also { _showItemList.value = it }
    }

    enum class ShoeState {
        Valid,
        InValid
    }
}
