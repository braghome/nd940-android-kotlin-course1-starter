package com.udacity.shoestore.models

import java.lang.Character.toLowerCase
import java.lang.Character.valueOf

class Validator {
    companion object {
        const val SHOE_DETAILS_ERROR = "Must contain only letters numbers and underscores"
        private const val SHOE_NAME_MSG = "The shoe name is empty"
        private const val COMPANY_MSG = "The company name empty"
        private const val SHOE_SIZE_MSG = "The shoe size can't be less than one or greater than 37"
        private const val DESCRIPTION_MSG = "The shoe description can't be empty"
        private const val ALL_PASS = "All fields valid"
        val charList = listOf<Char>(
                ',', '?', '.', '\'', ',', ';', '\"', '$', ':', '-'
        )
        val isValidCharacters: (Char) -> Boolean = { c -> charList.contains(toLowerCase(c)) }
        val hasAlphaNumericUnderscore: (String) -> Boolean = { s: String ->
            val allValid = s.all { ch -> valueOf(ch).isLetterOrDigit() && valueOf(ch).isWhitespace() ||
                    valueOf(ch).isLetterOrDigit() && valueOf(ch) == '_'
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
            class CheckName(val shoe: Shoe) : ShoeError()
            class CheckSize(val shoe: Shoe) : ShoeError()
            class CheckCompany(val shoe: Shoe) : ShoeError()
            class CheckDescription(val shoe: Shoe) : ShoeError()
        }

        val isNameError: (Shoe) -> Boolean = { it -> it.name.isNotBlank() || it.name.isBlank() }
        val isSizeError: (Shoe) -> Boolean = { it -> it.size < 1 || it.size > 37 }
        val isCompanyError: (Shoe) -> Boolean = { it -> it.company.isNotBlank() || it.company.isEmpty() }
        val isDescriptionError: (Shoe) -> Boolean = { it ->
            it.description.isEmpty() || it.description.isBlank()
        }
        val predicateList = listOf<(Shoe) -> Boolean>(
                isNameError, isSizeError, isCompanyError,
                isDescriptionError
        )

        private fun execute(shoe: Shoe, se: ShoeError) = when (se) {
            is ShoeError.CheckName -> if (isNameError(shoe)) {
                ShoeValidation.SHOE_NAME_EMPTY.msg.also { shoe.nameError = it }
                ShoeValidation.SHOE_NAME_EMPTY
            } else ShoeValidation.NO_ERRORS
            is ShoeError.CheckSize -> if (isSizeError(shoe)) {
                ShoeValidation.SHOE_SIZE_INVALID.msg.also { shoe.sizeError = it }
                ShoeValidation.SHOE_SIZE_INVALID
            } else ShoeValidation.NO_ERRORS
            is ShoeError.CheckCompany -> if (isCompanyError(shoe)) {
                ShoeValidation.COMPANY_NAME_EMPTY.msg.also { shoe.companyError = it }
                ShoeValidation.COMPANY_NAME_EMPTY
            } else ShoeValidation.NO_ERRORS
            is ShoeError.CheckDescription -> if (isDescriptionError(shoe)) {
                ShoeValidation.DESCRIPTION_EMPTY.msg.also { shoe.descriptionError = it }
                ShoeValidation.DESCRIPTION_EMPTY
            } else ShoeValidation.NO_ERRORS
        }

        class ShErr(val shErrOps: List<ShoeError> = emptyList()) {
            operator fun plus(shoeError: ShoeError) = ShErr(shErrOps + shoeError)
        }

        fun runShoe(shoe: Shoe, sheErr: ShErr) {
            sheErr.shErrOps.forEach { execute(shoe, it) }
        }
    }
}