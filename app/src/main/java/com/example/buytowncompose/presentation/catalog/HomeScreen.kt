package com.example.buytowncompose.presentation.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Close
import com.example.buytowncompose.domain.model.Product
import com.example.buytowncompose.presentation.cart.CartUiState

@Composable
fun HomeScreen(query: String, products: List<Product>, cart: CartUiState, onSearch: (String) -> Unit,
    onProduct: (Int) -> Unit, onIncrease: (Int) -> Unit, onDecrease: (Int) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Главная", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(horizontal = 20.dp))
        OutlinedTextField(value = query, onValueChange = onSearch, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp).testTag("home.search"),
            placeholder = { Text("Найти товары") }, leadingIcon = { Icon(Icons.Outlined.Search, null) },
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { onSearch("") }) { Icon(Icons.Outlined.Close, "Очистить поиск") } })
        Text(if (query.isBlank()) "Подобрали для вас" else "Найдено: ${products.size}",
            modifier = Modifier.padding(horizontal = 20.dp), style = MaterialTheme.typography.titleMedium)
        if (products.isEmpty()) Text("Ничего не найдено. Попробуйте другое название или категорию.", modifier = Modifier.padding(20.dp))
        ProductGrid(products, cart, onProduct, onIncrease, onDecrease, Modifier.weight(1f))
    }
}
