package com.example.buytowncompose.presentation.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.*
import com.example.buytowncompose.domain.model.Product
import com.example.buytowncompose.presentation.cart.CartUiState
import com.example.buytowncompose.presentation.common.*

@Composable
fun ProductGrid(products: List<Product>, cart: CartUiState, onProduct: (Int) -> Unit,
    onIncrease: (Int) -> Unit, onDecrease: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier.testTag("products.grid"),
        contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(products, key = { it.id }) { product ->
            Card(onClick = { onProduct(product.id) }, modifier = Modifier.fillMaxWidth().testTag("product:" + product.id)) {
                ProductImage(product, 132)
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(product.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(product.name, style = MaterialTheme.typography.titleMedium)
                    Text(formatPrice(product.price), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    CartQuantityControl(cart.quantity(product.id), { onDecrease(product.id) }, { onIncrease(product.id) })
                }
            }
        }
    }
}
