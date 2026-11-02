package com.example.buytowncompose.data.repository

import com.example.buytowncompose.domain.repository.CartRepository
import com.example.buytowncompose.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** One cart per user for the lifetime of the application process. */
class InMemoryCartRepository(products: ProductRepository) : CartRepository {
    private val productIds = products.products.map { it.id }.toSet()
    private val carts = mutableMapOf<String, MutableStateFlow<Map<Int, Int>>>()
    private fun cart(login: String) = carts.getOrPut(login) { MutableStateFlow(emptyMap()) }

    override fun observe(login: String): StateFlow<Map<Int, Int>> = cart(login).asStateFlow()

    override fun changeQuantity(login: String, productId: Int, delta: Int) {
        if (productId !in productIds) return
        cart(login).update { quantities ->
            val next = ((quantities[productId] ?: 0).toLong() + delta).coerceIn(0, 99).toInt()
            if (next == 0) quantities - productId else quantities + (productId to next)
        }
    }

    override fun remove(login: String, productId: Int) {
        cart(login).update { it - productId }
    }
}
