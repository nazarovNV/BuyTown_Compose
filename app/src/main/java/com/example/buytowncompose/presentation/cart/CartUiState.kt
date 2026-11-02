package com.example.buytowncompose.presentation.cart

import com.example.buytowncompose.domain.model.CartItem

data class CartUiState(val items: List<CartItem> = emptyList()) {
    val count get() = items.sumOf { it.quantity }
    val total get() = items.sumOf { it.subtotal }
    fun quantity(productId: Int) = items.find { it.product.id == productId }?.quantity ?: 0
}
