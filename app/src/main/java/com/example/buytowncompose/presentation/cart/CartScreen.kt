package com.example.buytowncompose.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.presentation.common.*

@Composable
fun CartScreen(state: CartUiState, onProduct: (Int) -> Unit, onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit, onRemove: (Int) -> Unit, onShopping: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Корзина", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(horizontal = 20.dp))
        if (state.items.isEmpty()) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Ваша корзина пока пуста. Добавьте понравившиеся товары из каталога.")
                Button(onClick = onShopping) { Text("К товарам") }
            }
        } else {
            LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.items, key = { it.product.id }) { item ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { onProduct(item.product.id) }) { Text(item.product.name) }
                            Text("${formatPrice(item.product.price)} за шт. • ${formatPrice(item.subtotal)}")
                            CartQuantityControl(item.quantity, { onDecrease(item.product.id) }, { onIncrease(item.product.id) })
                            TextButton(onClick = { onRemove(item.product.id) }) { Text("Удалить") }
                        }
                    }
                }
            }
            Text("Итого: ${formatPrice(state.total)}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(20.dp))
        }
    }
}
