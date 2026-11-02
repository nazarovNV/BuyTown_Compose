package com.example.buytowncompose.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.buytowncompose.domain.repository.AuthRepository

/** Owns feature ViewModels for one authenticated session, including configuration changes. */
class AppViewModel(private val repository: AuthRepository) : ViewModel(), ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
    val user = repository.user
    fun logout() {
        viewModelStore.clear()
        repository.logout()
    }
    override fun onCleared() { viewModelStore.clear() }
}
