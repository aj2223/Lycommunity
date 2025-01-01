package com.project.lycommunity.util

data class LoginValidation(
    val isValid: Boolean,
    val errorMessage: String? = null
)

class LoginValidationHelper {
    private fun validateEmail(email: String): LoginValidation {
        if (email.isEmpty()) {
            return LoginValidation(
                isValid = false,
                errorMessage = "Email is required"
            )
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return LoginValidation(
                isValid = false,
                errorMessage = "Invalid email format"
            )
        }

        return LoginValidation(isValid = true)
    }

    private fun validatePassword(password: String): LoginValidation {
        if (password.isEmpty()) {
            return LoginValidation(
                isValid = false,
                errorMessage = "Password is required"
            )
        }

        if (password.length < 6) {
            return LoginValidation(
                isValid = false,
                errorMessage = "Password must be at least 6 characters"
            )
        }

        return LoginValidation(isValid = true)
    }

    fun validateCredentials(email: String, password: String): Pair<LoginValidation, LoginValidation> {
        val emailResult = validateEmail(email)
        val passwordResult = validatePassword(password)
        return Pair(emailResult, passwordResult)
    }
}