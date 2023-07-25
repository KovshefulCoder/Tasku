package ru.kovsh.tasku.ui.auth.viewModels

import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates

object Validations {
    fun validatePassword(password: String): ValidityStates {
        return if (password.isEmpty()) {
            ValidityStates.Empty
        } else {
            if (password.length <= 6) {
                ValidityStates.Invalid
            } else if (password.length >= 20) {
                ValidityStates.Invalid
            } else if (!password.any { it.isDigit() }) {
                ValidityStates.Invalid
            } else if (!password.any { it.isLetter() }) {
                ValidityStates.Invalid
            } else
                ValidityStates.Valid
        }
    }

    fun validateRepeatedPassword(password: String, repeatedPassword: String): ValidityStates {
        return if (repeatedPassword.isEmpty()) {
            ValidityStates.Empty
        } else {
            if (repeatedPassword == password) {
                ValidityStates.Valid
            } else {
                ValidityStates.Invalid
            }
        }
    }

    fun validateEmail(email: String): ValidityStates {
        return if (email.isEmpty()) {
            ValidityStates.Empty
        } else {
            if (email.contains("@") && email.contains(".") && email.length >= 6) {
                val dogIndex = email.indexOf("@")
                val dotIndex = email.indexOfLast { it == '.' }
                if ((dogIndex != email.length - 1) && (dotIndex != email.length - 1) &&
                    (listOf<Char>(email[dogIndex-1], email[dogIndex+1], email[dotIndex-1],
                        email[dotIndex+1]).any{ it.isLetter() }))
                    ValidityStates.Valid
                else {
                    ValidityStates.Invalid
                }
            } else {
                ValidityStates.Invalid
            }
        }
    }

    fun approveValidation(vararg validityStates: ValidityStates): Boolean {
        return validityStates.all { it == ValidityStates.Valid }
    }
}