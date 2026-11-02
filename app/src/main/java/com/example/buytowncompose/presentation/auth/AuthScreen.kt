package com.example.buytowncompose.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(state: AuthUiState, onLogin: (String) -> Unit, onPassword: (String) -> Unit,
    onRepeat: (String) -> Unit, onSwitchMode: () -> Unit, onSubmit: () -> Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding()
        .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        ShoppingOtterLogo()
        Spacer(Modifier.height(12.dp))
        Text(if (state.isRegistration) "Регистрация" else "Авторизация", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text(if (state.isRegistration) "Создайте аккаунт, чтобы начать покупки" else "Войдите, чтобы найти что-нибудь для себя",
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(value = state.login, onValueChange = onLogin, label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth().testTag("auth.login"), singleLine = true, enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false), isError = state.loginError,
            supportingText = { if (state.loginError) Text("Введите логин") })
        OutlinedTextField(value = state.password, onValueChange = onPassword, label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth().testTag("auth.password"), singleLine = true, enabled = !state.isLoading,
            visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = state.passwordError, supportingText = { if (state.passwordError) Text("Введите пароль") })
        if (state.isRegistration) OutlinedTextField(value = state.repeatedPassword, onValueChange = onRepeat,
            label = { Text("Повторите пароль") }, modifier = Modifier.fillMaxWidth().testTag("auth.repeat"), singleLine = true, enabled = !state.isLoading,
            visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = state.repeatError, supportingText = { if (state.repeatError) Text("Пароли не совпадают") })
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onSubmit, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).testTag("auth.submit")) {
            Text(if (state.isLoading) "Подождите…" else if (state.isRegistration) "Зарегистрироваться" else "Войти")
        }
        Spacer(Modifier.height(24.dp))
        Text(if (state.isRegistration) "Вы уже зарегистрированы?" else "Хотите зарегистрироваться?")
        TextButton(onClick = onSwitchMode, enabled = !state.isLoading, modifier = Modifier.testTag("auth.switch")) { Text(if (state.isRegistration) "Войти" else "Зарегистрироваться") }
        Text("Демо-версия: данные не отправляются на сервер", style = MaterialTheme.typography.bodySmall)
        if (!state.isRegistration) Text("Тестовый вход: demo / demo123", modifier = Modifier.padding(top = 12.dp))
    }
}
