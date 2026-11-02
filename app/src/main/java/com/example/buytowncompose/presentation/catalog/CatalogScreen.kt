package com.example.buytowncompose.presentation.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.domain.model.Product
import com.example.buytowncompose.presentation.common.ProductImage

@Composable
fun CatalogScreen(categories: Map<String, List<Product>>, onCategory: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Каталог", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(horizontal = 20.dp))
        Text("Выберите категорию", modifier = Modifier.padding(horizontal = 20.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(categories.keys.toList(), key = { it }) { name ->
                Card(onClick = { onCategory(name) }) {
                    ProductImage(categories.getValue(name).first(), 100)
                    Column(Modifier.padding(12.dp)) {
                        Text(name, style = MaterialTheme.typography.titleMedium)
                        Text("Товаров: ${categories.getValue(name).size}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
