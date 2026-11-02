package com.example.buytowncompose.ui

import androidx.compose.ui.test.*
import org.junit.Test

class CatalogUiTest : BaseUiTest() {
    @Test fun searchIsCaseInsensitiveAndClearRestoresProducts() {
        login()
        search("  НАУШНИКИ  ")
        compose.onNodeWithText("Найдено: 1").assertExists()
        compose.onNodeWithTag("product:0").assertExists()
        compose.onNodeWithTag("product:1").assertDoesNotExist()
        compose.onNodeWithContentDescription("Очистить поиск").performClick()
        compose.onNodeWithText("Подобрали для вас").assertExists()
        showProduct(1)
        compose.onNodeWithTag("product:1").assertIsDisplayed()
    }

    @Test fun searchMatchesCategoryAndDescriptionAndHandlesNoResults() {
        login()
        search("Электроника")
        compose.onNodeWithText("Найдено: 3").assertExists()
        search("мягкие амбушюры")
        compose.onNodeWithText("Найдено: 1").assertExists()
        search("несуществующий товар 123")
        compose.onNodeWithText("Найдено: 0").assertExists()
        compose.onNodeWithText("Ничего не найдено.", substring = true).assertIsDisplayed()
        compose.onNodeWithTag("product:0").assertDoesNotExist()
    }

    @Test fun everyCategoryOpensItsProductsAndBackReturnsToCatalog() {
        login()
        tapTag("tab:CATALOG")
        val categories = mapOf("Электроника" to 0, "Аксессуары" to 1, "Для дома" to 2,
            "Посуда" to 3, "Книги" to 5, "Мебель" to 6)
        categories.forEach { (name, id) ->
            compose.onNodeWithTag("categories.grid").performScrollToNode(hasTestTag("category:$name"))
            tapTag("category:$name")
            showProduct(id)
            compose.onNodeWithTag("product:$id").assertIsDisplayed()
            if (name != "Электроника") compose.onNodeWithTag("product:0").assertDoesNotExist()
            back()
            compose.onNodeWithTag("categories.grid").assertExists()
        }
    }

    @Test fun detailsContainDescriptionSpecificationsPriceAndReturnToSearch() {
        login()
        search("Наушники")
        tapTag("product:0")
        compose.onNodeWithText("Описание").assertExists()
        compose.onNodeWithText("Характеристики").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Bluetooth 5.3", substring = true).assertExists()
        compose.onNodeWithText("3\u00a0490 ₽").assertExists()
        back()
        compose.onNodeWithTag("home.search").assertTextContains("Наушники")
        compose.onNodeWithTag("product:0").assertIsDisplayed()
    }

    @Test fun bottomTabsAndSystemBackWorkWithoutLoggingOut() {
        login()
        tapTag("tab:CATALOG")
        compose.onNodeWithTag("tab:CATALOG").assertIsSelected()
        tapTag("tab:PROFILE")
        compose.onNodeWithTag("tab:PROFILE").assertIsSelected()
        back()
        compose.onNodeWithTag("tab:HOME").assertIsSelected()
        compose.onNodeWithTag("home.search").assertExists()
    }
}
