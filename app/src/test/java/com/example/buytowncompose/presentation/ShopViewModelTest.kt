package com.example.buytowncompose.presentation

import com.example.buytowncompose.data.repository.DemoProductRepository
import com.example.buytowncompose.presentation.shop.*
import org.junit.Assert.*
import org.junit.Test

class ShopViewModelTest {
    @Test
    fun searchMatchesNamesCategoriesAndDescriptionsIgnoringCase() {
        val vm = ShopViewModel(DemoProductRepository())
        vm.search("  НАУШНИКИ  ")
        assertEquals(listOf("Наушники"), vm.state.value.searchResults.map { it.name })
        vm.search("Электроника")
        assertEquals(3, vm.state.value.searchResults.size)
        vm.search("мягкие амбушюры")
        assertEquals(1, vm.state.value.searchResults.size)
        vm.search("такого товара нет")
        assertTrue(vm.state.value.searchResults.isEmpty())
    }

    @Test
    fun backRestoresCategoryAndCartRatherThanResettingToLogin() {
        val vm = ShopViewModel(DemoProductRepository())
        vm.selectTab(MainTab.CATALOG)
        vm.openCategory("Книги")
        vm.openProduct(5)
        vm.openCart()
        vm.openProduct(0)
        vm.back()
        assertEquals(ShopDestination.Cart, vm.state.value.destination)
        vm.back()
        assertEquals(ShopDestination.Details(5), vm.state.value.destination)
        vm.back()
        assertEquals(ShopDestination.Category("Книги"), vm.state.value.destination)
        vm.back()
        assertEquals(ShopDestination.Catalog, vm.state.value.destination)
        vm.back()
        assertEquals(MainTab.HOME, vm.state.value.tab)
        assertFalse(vm.state.value.canGoBack)
    }
}
