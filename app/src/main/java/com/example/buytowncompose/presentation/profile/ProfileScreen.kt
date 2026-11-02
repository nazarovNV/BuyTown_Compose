package com.example.buytowncompose.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(state: ProfileUiState, onName: (String) -> Unit, onAvatar: (Int) -> Unit,
    onSave: () -> Unit, onRetry: () -> Unit, onLogout: () -> Unit) {
    val avatars = listOf(Icons.Outlined.Person, Icons.Outlined.Face, Icons.Outlined.Pets, Icons.Outlined.SportsEsports)
    val avatarNames = listOf("Пользователь", "Улыбка", "Лапка", "Геймпад")

    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Профиль", style = MaterialTheme.typography.headlineLarge)
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Icon(avatars[state.avatar], contentDescription = avatarNames[state.avatar],
                modifier = Modifier.padding(24.dp).size(72.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Text("Аккаунт • " + state.login, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Выберите аватарку", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            avatars.forEachIndexed { index, icon ->
                FilledTonalIconToggleButton(modifier = Modifier.testTag("profile.avatar:" + index), checked = state.avatar == index, enabled = !state.isLoading && !state.isSaving, onCheckedChange = { onAvatar(index) }) {
                    Icon(icon, contentDescription = avatarNames[index])
                }
            }
        }
        OutlinedTextField(value = state.name, onValueChange = onName, enabled = !state.isLoading && !state.isSaving, label = { Text("Ваше имя") },
            modifier = Modifier.fillMaxWidth().testTag("profile.name"), singleLine = true, isError = state.nameError,
            supportingText = { if (state.nameError) Text("Введите имя") })
        Button(onClick = onSave, enabled = !state.isLoading && !state.isSaving,
            modifier = Modifier.fillMaxWidth().testTag("profile.save")) { Text(if (state.isSaving) "Сохранение…" else "Сохранить изменения") }
        if (state.isLoading) CircularProgressIndicator()
        if (state.saved) Text("Профиль сохранён на устройстве", color = MaterialTheme.colorScheme.primary)
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onRetry) { Text("Загрузить заново") }
        }
        Text("О себе", style = MaterialTheme.typography.titleMedium)
        Text("Добро пожаловать в BuyTown! Здесь можно искать товары, изучать категории и собирать свою корзину.")
        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth().testTag("profile.logout")) { Text("Выйти из аккаунта") }
    }
}
