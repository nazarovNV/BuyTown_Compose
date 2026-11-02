package com.example.buytowncompose.data.repository

import com.example.buytowncompose.data.source.LocalAccounts
import com.example.buytowncompose.domain.model.User
import com.example.buytowncompose.domain.repository.AuthRepository
import com.example.buytowncompose.domain.repository.RegistrationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class LocalAuthRepository(
    private val accounts: LocalAccounts,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {
    private val currentUser = MutableStateFlow<User?>(null)
    override val user = currentUser.asStateFlow()

    override suspend fun register(login: String, password: String): RegistrationResult = withContext(ioDispatcher) {
        when {
            accounts.exists(login) -> RegistrationResult.LOGIN_TAKEN
            accounts.register(login, password) -> RegistrationResult.SUCCESS
            else -> RegistrationResult.STORAGE_ERROR
        }
    }

    override suspend fun login(login: String, password: String): Boolean = withContext(ioDispatcher) {
        accounts.authenticate(login, password).also { valid ->
            if (valid) currentUser.value = User(login.trim())
        }
    }

    override fun logout() { currentUser.value = null }
}
