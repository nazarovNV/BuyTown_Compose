package com.example.buytowncompose.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buytowncompose.domain.model.CartItem
import com.example.buytowncompose.domain.repository.CartRepository
import com.example.buytowncompose.domain.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel(
    private val login: String,
    private val repository: CartRepository,
    products: ProductRepository
) : ViewModel() {
    val state = repository.observe(login).map { quantities ->
        CartUiState(products.products.mapNotNull { product ->
            quantities[product.id]?.takeIf { it > 0 }?.let { CartItem(product, it) }
        })
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CartUiState())

    fun increase(id: Int) = repository.changeQuantity(login, id, 1)
    fun decrease(id: Int) = repository.changeQuantity(login, id, -1)
    fun remove(id: Int) = repository.remove(login, id)
}
