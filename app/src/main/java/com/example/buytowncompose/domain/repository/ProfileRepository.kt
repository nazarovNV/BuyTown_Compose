package com.example.buytowncompose.domain.repository

import com.example.buytowncompose.domain.model.Profile

interface ProfileRepository {
    suspend fun load(login: String): Profile
    suspend fun save(login: String, profile: Profile): Boolean
}
