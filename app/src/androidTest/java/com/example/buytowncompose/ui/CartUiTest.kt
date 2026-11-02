package com.example.buytowncompose.ui

import androidx.compose.ui.test.*
import org.junit.Test

class CartUiTest : BaseUiTest() {
    @Test fun emptyCartOffersReturnToShopping() {
        login()
        cart()
        compose.onNodeWithText("Ваша корзина пока пуста.", substring = true).assertIsDisplayed()
        tapText("К товарам")
        compose.onNodeWithTag("tab:HOME").assertIsSelected()
    }

    @Test fun gridButtonBecomesCounterAndZeroRestoresAddButton() {
        login()
        search("Наушники")
        add(0)
        assertQuantity(0, 1)
        increase(0)
        assertQuantity(0, 2)
        decrease(0)
        decrease(0)
        productAction(0, hasText("В корзину")).assertExists()
        compose.onNodeWithTag("shop.cart").assertTextContains("Корзина (0)")
    }

    @Test fun detailsAndGridShareTheSameQuantity() {
        login()
        search("Наушники")
        tapTag("product:0")
        productAction(0, hasText("В корзину")).performScrollTo().performClick()
        increase(0)
        assertQuantity(0, 2)
        back()
        assertQuantity(0, 2)
        decrease(0)
        cart()
        assertQuantity(0, 1)
    }

    @Test fun cartTotalUpdatesForMultipleProductsAndExplicitRemoval() {
        login()
        add(0)
        increase(0)
        add(1)
        cart()
        compose.onNodeWithTag("cart.total").assertTextEquals("Итого: 9\u00a0770 ₽")
        productAction(1, hasText("Удалить")).performScrollTo().performClick()
        compose.onNodeWithTag("cart.total").assertTextEquals("Итого: 6\u00a0980 ₽")
        decrease(0)
        compose.onNodeWithTag("cart.total").assertTextEquals("Итого: 3\u00a0490 ₽")
        decrease(0)
        compose.onNodeWithText("Ваша корзина пока пуста.", substring = true).assertExists()
        compose.onNodeWithTag("cart.total").assertDoesNotExist()
    }

    @Test fun openingDetailsFromCartAndBackRestoresCart() {
        login()
        add(0)
        cart()
        tapText("Наушники")
        compose.onNodeWithText("Описание").assertExists()
        increase(0)
        back()
        assertQuantity(0, 2)
        compose.onNodeWithTag("cart.total").assertExists()
    }

    @Test fun cartAndNavigationSurviveActivityRecreation() {
        login()
        add(0)
        cart()
        increase(0)
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("cart.total").assertExists()
        assertQuantity(0, 2)
        back()
        compose.onNodeWithTag("home.search").assertExists()
    }

    @Test fun cartDoesNotLeakToAnotherAccount() {
        val first = login()
        add(0)
        logout()
        val second = seedAccount()
        login(second)
        cart()
        compose.onNodeWithText("Ваша корзина пока пуста.", substring = true).assertExists()
        logout()
        login(first)
        cart()
        assertQuantity(0, 1)
    }
}
