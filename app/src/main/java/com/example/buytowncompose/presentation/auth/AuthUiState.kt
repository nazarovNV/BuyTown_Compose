package com.example.buytowncompose.presentation.auth

data class AuthUiState(
    val isRegistration: Boolean = false,
    val login: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val submitted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
) {
    val loginError get() = submitted && login.isBlank()
    val passwordError get() = submitted && password.isBlank()
    val repeatError get() = submitted && isRegistration && password != repeatedPassword
}
