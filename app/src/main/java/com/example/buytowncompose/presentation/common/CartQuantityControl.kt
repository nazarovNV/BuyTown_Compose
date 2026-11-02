package com.example.buytowncompose.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove

@Composable
fun CartQuantityControl(quantity: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    if (quantity == 0) {
        Button(
            onClick = onIncrease,
            shape = shape,
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) { Text("В корзину") }
    } else {
        Surface(shape = shape, color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onDecrease) {
                    Icon(Icons.Outlined.Remove, contentDescription = "Уменьшить количество")
                }
                Text("$quantity", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onIncrease, enabled = quantity < 99) {
                    Icon(Icons.Outlined.Add, contentDescription = "Увеличить количество")
                }
            }
        }
    }
}
