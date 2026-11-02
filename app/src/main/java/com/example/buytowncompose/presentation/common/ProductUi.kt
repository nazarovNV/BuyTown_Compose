package com.example.buytowncompose.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.buytowncompose.domain.model.ProductArt
import java.text.NumberFormat
import java.util.Locale

fun formatPrice(value: Int): String = NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU")).format(value) + " ₽"

val ProductArt.icon: ImageVector get() = when (this) {
    ProductArt.Headphones -> Icons.Outlined.Headphones
    ProductArt.Backpack -> Icons.Outlined.Backpack
    ProductArt.Lightbulb -> Icons.Outlined.Lightbulb
    ProductArt.Coffee -> Icons.Outlined.Coffee
    ProductArt.Watch -> Icons.Outlined.Watch
    ProductArt.MenuBook -> Icons.AutoMirrored.Outlined.MenuBook
    ProductArt.Weekend -> Icons.Outlined.Weekend
    ProductArt.PhotoCamera -> Icons.Outlined.PhotoCamera
}
