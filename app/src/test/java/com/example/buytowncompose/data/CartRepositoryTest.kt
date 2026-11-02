package com.example.buytowncompose.data

import com.example.buytowncompose.data.repository.DemoProductRepository
import com.example.buytowncompose.data.repository.InMemoryCartRepository
import com.example.buytowncompose.domain.model.CartItem
import com.example.buytowncompose.presentation.cart.CartUiState
import org.junit.Assert.*
import org.junit.Test

class CartRepositoryTest {
    private val products = DemoProductRepository()
    private val cart = InMemoryCartRepository(products)

    @Test
    fun quantitiesAreBoundedAndZeroRemovesTheItem() {
        cart.changeQuantity("V", 0, 1)
        assertEquals(1, cart.observe("V").value[0])
        cart.changeQuantity("V", 0, Int.MAX_VALUE)
        assertEquals(99, cart.observe("V").value[0])
        cart.changeQuantity("V", 0, Int.MIN_VALUE)
        assertFalse(cart.observe("V").value.containsKey(0))
        cart.changeQuantity("V", -1, 1)
        assertTrue(cart.observe("V").value.isEmpty())
    }

    @Test
    fun cartsAreIsolatedBetweenAccountsAndTotalsMatchItems() {
        cart.changeQuantity("V", 0, 2)
        cart.changeQuantity("V", 1, 1)
        assertTrue(cart.observe("demo").value.isEmpty())
        val state = CartUiState(products.products.mapNotNull { product ->
            cart.observe("V").value[product.id]?.let { CartItem(product, it) }
        })
        assertEquals(3, state.count)
        assertEquals(3490 * 2 + 2790, state.total)
        cart.remove("V", 0)
        assertEquals(mapOf(1 to 1), cart.observe("V").value)
    }
}
