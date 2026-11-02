package com.example.buytowncompose.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.buytowncompose.di.AppContainer
import com.example.buytowncompose.presentation.auth.AuthScreen
import com.example.buytowncompose.presentation.auth.AuthViewModel
import com.example.buytowncompose.presentation.shop.ShopRoute

@Composable
fun AppRoute(container: AppContainer) {
    val app: AppViewModel = viewModel(factory = viewModelFactory { initializer { AppViewModel(container.authRepository) } })
    val auth: AuthViewModel = viewModel(factory = viewModelFactory { initializer { AuthViewModel(container.authRepository) } })
    val user by app.user.collectAsStateWithLifecycle()
    val form by auth.state.collectAsStateWithLifecycle()
    val signedInUser = user
    if (signedInUser == null) {
        BackHandler(enabled = form.isRegistration) { auth.backToLogin() }
        AuthScreen(form, auth::loginChanged, auth::passwordChanged, auth::repeatChanged, auth::switchMode, auth::submit)
    } else {
        ShopRoute(container, signedInUser, app, app::logout)
    }
}
