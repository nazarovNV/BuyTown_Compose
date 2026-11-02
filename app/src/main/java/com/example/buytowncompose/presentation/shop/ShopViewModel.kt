package com.example.buytowncompose.presentation.shop

import androidx.lifecycle.ViewModel
import com.example.buytowncompose.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShopViewModel(repository: ProductRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(ShopUiState(products = repository.products))
    val state = mutableState.asStateFlow()
    fun search(value: String) { mutableState.update { it.copy(query = value) } }
    fun selectTab(tab: MainTab) {
        val destination = when (tab) {
            MainTab.HOME -> ShopDestination.Home
            MainTab.CATALOG -> ShopDestination.Catalog
            MainTab.PROFILE -> ShopDestination.Profile
        }
        mutableState.update { it.copy(tab = tab, backStack = listOf(destination)) }
    }
    private fun push(destination: ShopDestination) {
        mutableState.update { if (it.destination == destination) it else it.copy(backStack = it.backStack + destination) }
    }
    fun openCategory(name: String) { if (name in state.value.categories) push(ShopDestination.Category(name)) }
    fun openProduct(id: Int) { if (state.value.products.any { it.id == id }) push(ShopDestination.Details(id)) }
    fun openCart() = push(ShopDestination.Cart)
    fun back() {
        if (state.value.backStack.size > 1) mutableState.update { it.copy(backStack = it.backStack.dropLast(1)) }
        else selectTab(MainTab.HOME)
    }
}
