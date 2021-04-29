package com.udacity.shoestore.models

import java.lang.Character.valueOf

class Validator {
    companion object {
        const val SHOE_DETAILS_ERROR = "Must contain only letters numbers, underscores, or white space"
        private const val SHOE_NAME_MSG = "The shoe name is empty"
        private const val COMPANY_MSG = "The company name empty"
        private const val SHOE_SIZE_MSG = "The shoe size can't be less than one or greater than 37"
        private const val DESCRIPTION_MSG = "The shoe description can't be empty"
        const val ALL_PASS = ""
        private val charList = listOf(',', '?', '.', '\'', ',', ';', '\"', '$', ':', '-')
        val isValidCharacters: (Char) -> Boolean = { c -> charList.contains(c) }
        val hasAlphaNumericUnderscore: (String) -> Boolean = { s: String ->
            val allValid = s.all { ch -> valueOf(ch).isLetterOrDigit() || valueOf(ch).isWhitespace() ||
                    valueOf(ch) == '_'
            }
            allValid
        }
        val hasAlphaNumericUnderscoreOnDescription: (String) -> Boolean = { s: String ->
            val allValid = s.all { ch ->
                isValidCharacters(ch) && valueOf(ch).isLetterOrDigit() && valueOf(ch).isWhitespace() ||
                        valueOf(ch) == '_' || valueOf(ch).isWhitespace()
            }
            allValid
        }
        fun verify(shoeName: String): Boolean {
            return hasAlphaNumericUnderscore(shoeName)
        }
        fun verifyDescription(shoeName: String): Boolean {
            return hasAlphaNumericUnderscoreOnDescription(shoeName)
        }
        enum class ShoeValidation(val msg: String) {
            SHOE_NAME_EMPTY(SHOE_NAME_MSG),
            COMPANY_NAME_EMPTY(COMPANY_MSG),
            SHOE_SIZE_INVALID(SHOE_SIZE_MSG),
            DESCRIPTION_EMPTY(DESCRIPTION_MSG),
            NO_ERRORS(ALL_PASS)
        }

        sealed class ShoeError {
            class CheckName(val shoeWithError: ShoeWithError) : ShoeError()
            class CheckSize(val shoeWithError: ShoeWithError) : ShoeError()
            class CheckCompany(val shoeWithError: ShoeWithError) : ShoeError()
            class CheckDescription(val shoeWithError: ShoeWithError) : ShoeError()
        }

        val isNameError: (ShoeWithError) -> Boolean = { it -> it.name.isEmpty() }
        val isSizeError: (ShoeWithError) -> Boolean = { it -> it.size < 1 || it.size > 37 }
        val isCompanyError: (ShoeWithError) -> Boolean = { it -> it.company.isEmpty() }
        val isDescriptionError: (ShoeWithError) -> Boolean = { it -> it.description.isEmpty() }

        private fun execute(shoeWithError: ShoeWithError, se: ShoeError) = when (se) {
            is ShoeError.CheckName -> if (isNameError(shoeWithError)) {
                ShoeValidation.SHOE_NAME_EMPTY.msg.also { shoeWithError.nameError = it }
                ShoeValidation.SHOE_NAME_EMPTY
            } else {
                ALL_PASS.also { shoeWithError.nameError = it }
                ShoeValidation.NO_ERRORS
            }
            is ShoeError.CheckSize -> if (isSizeError(shoeWithError)) {
                ShoeValidation.SHOE_SIZE_INVALID.msg.also { shoeWithError.sizeError = it }
                ShoeValidation.SHOE_SIZE_INVALID
            } else {
                ALL_PASS.also { shoeWithError.sizeError = it }
                ShoeValidation.NO_ERRORS
            }
            is ShoeError.CheckCompany -> if (isCompanyError(shoeWithError)) {
                ShoeValidation.COMPANY_NAME_EMPTY.msg.also { shoeWithError.companyError = it }
                ShoeValidation.COMPANY_NAME_EMPTY
            } else {
                ALL_PASS.also { shoeWithError.companyError = it }
                ShoeValidation.NO_ERRORS
            }
            is ShoeError.CheckDescription -> if (isDescriptionError(shoeWithError)) {
                ShoeValidation.DESCRIPTION_EMPTY.msg.also { shoeWithError.descriptionError = it }
                ShoeValidation.DESCRIPTION_EMPTY
            } else {
                ALL_PASS.also { shoeWithError.descriptionError = it }
                ShoeValidation.NO_ERRORS
            }
        }

        class ShErr(val shErrOps: List<ShoeError> = emptyList()) {
            operator fun plus(shoeError: ShoeError) = ShErr(shErrOps + shoeError)
        }

        fun runShoe(shoeWithError: ShoeWithError, sheErr: ShErr) {
            sheErr.shErrOps.forEach { execute(shoeWithError, it) }
        }
    }
}