package com.example.buytowncompose.domain.repository

import com.example.buytowncompose.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val user: StateFlow<User?>
    suspend fun register(login: String, password: String): RegistrationResult
    suspend fun login(login: String, password: String): Boolean
    fun logout()
}
