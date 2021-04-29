package com.udacity.shoestore.models


data class ShoeWithError(var name: String, var size: Int, var company: String, var description: String,
                         var nameError: String = "", var sizeError: String = "",
                         var companyError: String = "", var descriptionError: String = "") {
    val noError: Boolean
        get() = name.isNotBlank() && size > 0 && size < 38 && company.isNotBlank()
                && description.isNotBlank() && nameError.isBlank() && sizeError.isBlank()
                && companyError.isBlank() && descriptionError.isBlank()
}