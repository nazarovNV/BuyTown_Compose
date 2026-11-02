package com.example.buytowncompose.domain.repository

import com.example.buytowncompose.domain.model.Product

interface ProductRepository {
    val products: List<Product>
}
