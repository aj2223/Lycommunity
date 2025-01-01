

data class SignUpValidation(
    val isValid: Boolean,
    val errorMessage: String? = null
)

class SignUpValidationHelper {

    /**
     * Validates the email.
     */
    private fun validateEmail(email: String): SignUpValidation {
        if (email.isBlank()) {
            return SignUpValidation(
                isValid = false,
                errorMessage = "Email is required"
            )
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return SignUpValidation(
                isValid = false,
                errorMessage = "Invalid email format"
            )
        }

        return SignUpValidation(isValid = true)
    }

    /**
     * Validates the password.
     */
    private fun validatePassword(password: String): SignUpValidation {
        if (password.isBlank()) {
            return SignUpValidation(
                isValid = false,
                errorMessage = "Password is required"
            )
        }

        if (password.length < 6) {
            return SignUpValidation(
                isValid = false,
                errorMessage = "Password must be at least 6 characters"
            )
        }

        return SignUpValidation(isValid = true)
    }

    /**
     * Validates non-email, non-password fields.
     */
    private fun validateField(field: String, fieldName: String): SignUpValidation {
        if (field.isBlank()) {
            return SignUpValidation(
                isValid = false,
                errorMessage = "$fieldName is required"
            )
        }

        return SignUpValidation(isValid = true)
    }

    /**
     * Validates all sign-up credentials.
     */
    fun validateAllFields(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        department: String,
        idNumber: String
    ): List<SignUpValidation> {
        return listOf(
            validateEmail(email),
            validatePassword(password),
            validateField(firstName, "First name"),
            validateField(lastName, "Last name"),
            validateField(department, "Department"),
            validateField(idNumber, "ID number")
        )
    }
}
