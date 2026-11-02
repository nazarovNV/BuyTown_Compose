package com.example.buytowncompose.presentation.shop

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.presentation.cart.*
import com.example.buytowncompose.presentation.catalog.*

@Composable
fun ShopScreen(state: ShopUiState, cart: CartUiState,
    onTab: (MainTab) -> Unit, onBack: () -> Unit, onSearch: (String) -> Unit,
    onCategory: (String) -> Unit, onProduct: (Int) -> Unit, onCart: () -> Unit,
    onIncrease: (Int) -> Unit, onDecrease: (Int) -> Unit, onRemove: (Int) -> Unit,
    profileContent: @Composable () -> Unit) {
    BackHandler(enabled = state.canGoBack, onBack = onBack)
    Column(Modifier.fillMaxSize().safeDrawingPadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            if (state.backStack.size > 1) TextButton(onClick = onBack) { Text("Назад") }
            else Text("BuyTown", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            if (state.destination != ShopDestination.Cart) TextButton(onClick = onCart) { Text("Корзина (${cart.count})") }
        }
        Box(Modifier.weight(1f).fillMaxWidth()) {
            when (val destination = state.destination) {
                ShopDestination.Home -> HomeScreen(state.query, state.searchResults, cart, onSearch, onProduct, onIncrease, onDecrease)
                ShopDestination.Catalog -> CatalogScreen(state.categories, onCategory)
                ShopDestination.Profile -> profileContent()
                is ShopDestination.Category -> CategoryScreen(destination.name, state.categories[destination.name].orEmpty(), cart, onProduct, onIncrease, onDecrease)
                is ShopDestination.Details -> state.products.find { it.id == destination.productId }?.let {
                    ProductDetailsScreen(it, cart.quantity(it.id), { onIncrease(it.id) }, { onDecrease(it.id) })
                }
                ShopDestination.Cart -> CartScreen(cart, onProduct, onIncrease, onDecrease, onRemove, { onTab(MainTab.HOME) })
            }
        }
        NavigationBar {
            MainTab.entries.forEach { tab ->
                val label = when (tab) { MainTab.HOME -> "Главная"; MainTab.CATALOG -> "Каталог"; MainTab.PROFILE -> "Профиль" }
                val icon = when (tab) { MainTab.HOME -> Icons.Outlined.Home; MainTab.CATALOG -> Icons.Outlined.GridView; MainTab.PROFILE -> Icons.Outlined.Person }
                NavigationBarItem(selected = state.tab == tab, onClick = { onTab(tab) },
                    icon = { Icon(icon, contentDescription = null) }, label = { Text(label) })
            }
        }
    }
}
