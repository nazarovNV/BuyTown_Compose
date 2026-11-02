package com.example.buytowncompose.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    fun observe(login: String): StateFlow<Map<Int, Int>>
    fun changeQuantity(login: String, productId: Int, delta: Int)
    fun remove(login: String, productId: Int)
}
