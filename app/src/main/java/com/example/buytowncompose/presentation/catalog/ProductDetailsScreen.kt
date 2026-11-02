package com.example.buytowncompose.presentation.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.domain.model.Product
import com.example.buytowncompose.presentation.common.*

@Composable
fun ProductDetailsScreen(product: Product, quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Column(Modifier.fillMaxSize().testTag("product:" + product.id).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProductImage(product, 220)
        Text(product.category, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(product.name, style = MaterialTheme.typography.headlineLarge)
        Text(formatPrice(product.price), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Text("В наличии • Демонстрационный товар", style = MaterialTheme.typography.labelLarge)
        Text("Описание", style = MaterialTheme.typography.titleLarge)
        Text(product.description)
        Text("Характеристики", style = MaterialTheme.typography.titleLarge)
        Text(product.specifications)
        CartQuantityControl(quantity, onDecrease, onIncrease)
    }
}
