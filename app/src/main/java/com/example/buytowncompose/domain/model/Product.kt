package com.example.buytowncompose.domain.model

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Int,
    val art: ProductArt,
    val description: String,
    val specifications: String
)
