package com.example.buytowncompose.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buytowncompose.domain.model.Profile
import com.example.buytowncompose.domain.repository.ProfileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val login: String, private val repository: ProfileRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(ProfileUiState(login))
    val state = mutableState.asStateFlow()
    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        if (loadJob?.isActive == true || state.value.isSaving) return
        mutableState.update { it.copy(isLoading = true, error = null) }
        loadJob = viewModelScope.launch {
            try {
                val profile = repository.load(login)
                mutableState.update { it.copy(name = profile.name, avatar = profile.avatar) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.update { it.copy(error = "Не удалось загрузить профиль") }
            } finally {
                mutableState.update { it.copy(isLoading = false) }
            }
        }
    }
    fun nameChanged(value: String) { mutableState.update { it.copy(name = value, saved = false) } }
    fun avatarChanged(value: Int) { if (value in 0..3) mutableState.update { it.copy(avatar = value, saved = false) } }
    fun save() {
        if (state.value.isLoading || state.value.isSaving) return
        mutableState.update { it.copy(attempted = true, saved = false, error = null) }
        if (state.value.nameError) return
        val profile = Profile(state.value.name.trim(), state.value.avatar)
        mutableState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            try {
                if (repository.save(login, profile)) mutableState.update { it.copy(name = profile.name, saved = true) }
                else mutableState.update { it.copy(error = "Не удалось сохранить профиль") }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.update { it.copy(error = "Не удалось сохранить профиль") }
            } finally {
                mutableState.update { it.copy(isSaving = false) }
            }
        }
    }
}
