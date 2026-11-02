package com.example.buytowncompose.presentation.shop

sealed interface ShopDestination {
    data object Home : ShopDestination
    data object Catalog : ShopDestination
    data object Profile : ShopDestination
    data class Category(val name: String) : ShopDestination
    data class Details(val productId: Int) : ShopDestination
    data object Cart : ShopDestination
}

