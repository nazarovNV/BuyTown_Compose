package com.example.buytowncompose.data.repository

import android.content.Context
import com.example.buytowncompose.domain.model.Profile
import com.example.buytowncompose.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalProfileRepository(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProfileRepository {
    private val preferences = context.getSharedPreferences("demo_profile", Context.MODE_PRIVATE)

    override suspend fun load(login: String): Profile = withContext(ioDispatcher) {
        // Existing demo profile remains available after the migration.
        val legacyName = if (login == "demo") preferences.getString("name", "Покупатель") else login
        val legacyAvatar = if (login == "demo") preferences.getInt("avatar", 0) else 0
        Profile(
            name = preferences.getString("$login:name", legacyName).orEmpty(),
            avatar = preferences.getInt("$login:avatar", legacyAvatar).coerceIn(0, 3)
        )
    }

    override suspend fun save(login: String, profile: Profile): Boolean = withContext(ioDispatcher) {
        preferences.edit().putString("$login:name", profile.name)
            .putInt("$login:avatar", profile.avatar).commit()
    }
}
