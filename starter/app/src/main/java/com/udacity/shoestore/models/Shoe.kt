package com.udacity.shoestore.models


data class Shoe(var name: String, var size: Int, var company: String, var description: String,
                var nameError: String = "", var sizeError: String = "",
                var companyError: String = "", var descriptionError: String = "")