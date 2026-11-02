package com.example.buytowncompose.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.buytowncompose.domain.model.Product

@Composable
fun ProductImage(product: Product, height: Int) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.height(height.dp), contentAlignment = Alignment.Center) {
            Icon(product.art.icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

