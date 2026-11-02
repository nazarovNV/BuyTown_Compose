package com.example.buytowncompose.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buytowncompose.domain.repository.AuthRepository
import com.example.buytowncompose.domain.repository.RegistrationResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(AuthUiState())
    val state = mutableState.asStateFlow()

    fun loginChanged(value: String) { mutableState.update { it.copy(login = value, error = null) } }
    fun passwordChanged(value: String) { mutableState.update { it.copy(password = value, error = null) } }
    fun repeatChanged(value: String) { mutableState.update { it.copy(repeatedPassword = value, error = null) } }
    fun switchMode() {
        if (!state.value.isLoading) mutableState.value = AuthUiState(isRegistration = !state.value.isRegistration)
    }
    fun backToLogin() { if (!state.value.isLoading) mutableState.value = AuthUiState() }

    fun submit() {
        if (state.value.isLoading) return
        mutableState.update { it.copy(submitted = true, error = null, message = null) }
        val form = state.value
        if (form.loginError || form.passwordError || form.repeatError) return
        mutableState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                if (form.isRegistration) {
                    when (repository.register(form.login.trim(), form.password)) {
                        RegistrationResult.SUCCESS -> mutableState.value = AuthUiState(
                            login = form.login.trim(), message = "Аккаунт создан. Введите пароль, чтобы войти.")
                        RegistrationResult.LOGIN_TAKEN -> mutableState.update { it.copy(error = "Этот логин уже занят") }
                        RegistrationResult.STORAGE_ERROR -> mutableState.update { it.copy(error = "Не удалось сохранить аккаунт. Попробуйте ещё раз.") }
                    }
                } else if (repository.login(form.login.trim(), form.password)) {
                    mutableState.value = AuthUiState()
                } else {
                    mutableState.update { it.copy(error = "Неверный логин или пароль") }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.update { it.copy(error = "Не удалось выполнить вход или регистрацию. Попробуйте ещё раз.") }
            } finally {
                mutableState.update { it.copy(isLoading = false) }
            }
        }
    }
}
