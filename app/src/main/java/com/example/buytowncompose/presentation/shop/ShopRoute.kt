package com.example.buytowncompose.presentation.shop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.buytowncompose.di.AppContainer
import com.example.buytowncompose.domain.model.User
import com.example.buytowncompose.presentation.cart.CartViewModel
import com.example.buytowncompose.presentation.profile.ProfileScreen
import com.example.buytowncompose.presentation.profile.ProfileViewModel

@Composable
fun ShopRoute(container: AppContainer, user: User, owner: ViewModelStoreOwner, onLogout: () -> Unit) {
    val shop: ShopViewModel = viewModel(viewModelStoreOwner = owner, factory = viewModelFactory {
        initializer { ShopViewModel(container.productRepository) }
    })
    val cart: CartViewModel = viewModel(viewModelStoreOwner = owner, factory = viewModelFactory {
        initializer { CartViewModel(user.login, container.cartRepository, container.productRepository) }
    })
    val profile: ProfileViewModel = viewModel(viewModelStoreOwner = owner, factory = viewModelFactory {
        initializer { ProfileViewModel(user.login, container.profileRepository) }
    })
    val shopState by shop.state.collectAsStateWithLifecycle()
    val cartState by cart.state.collectAsStateWithLifecycle()
    val profileState by profile.state.collectAsStateWithLifecycle()
    ShopScreen(shopState, cartState, shop::selectTab, shop::back, shop::search,
        shop::openCategory, shop::openProduct, shop::openCart, cart::increase, cart::decrease, cart::remove) {
        ProfileScreen(profileState, profile::nameChanged, profile::avatarChanged, profile::save, profile::load, onLogout)
    }
}
