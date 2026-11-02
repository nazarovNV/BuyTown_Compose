package com.example.buytowncompose.presentation.profile

data class ProfileUiState(
    val login: String,
    val name: String = "",
    val avatar: Int = 0,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val attempted: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
) {
    val nameError get() = attempted && name.isBlank()
}
