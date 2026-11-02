package com.example.buytowncompose.presentation.shop

import com.example.buytowncompose.domain.model.Product

data class ShopUiState(
    val products: List<Product>,
    val tab: MainTab = MainTab.HOME,
    val backStack: List<ShopDestination> = listOf(ShopDestination.Home),
    val query: String = ""
) {
    val destination get() = backStack.last()
    val canGoBack get() = backStack.size > 1 || tab != MainTab.HOME
    val categories get() = products.groupBy { it.category }
    val searchResults get() = products.filter {
        query.isBlank() || listOf(it.name, it.category, it.description).any { text -> text.contains(query.trim(), ignoreCase = true) }
    }
}
