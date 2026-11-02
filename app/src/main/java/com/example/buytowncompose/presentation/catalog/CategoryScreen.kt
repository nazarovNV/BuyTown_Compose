package com.example.buytowncompose.presentation.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.domain.model.Product
import com.example.buytowncompose.presentation.cart.CartUiState

@Composable
fun CategoryScreen(name: String, products: List<Product>, cart: CartUiState,
    onProduct: (Int) -> Unit, onIncrease: (Int) -> Unit, onDecrease: (Int) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text(name, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(horizontal = 20.dp))
        ProductGrid(products, cart, onProduct, onIncrease, onDecrease, Modifier.weight(1f))
    }
}
