package com.example.buytowncompose.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.buytowncompose.MainActivity
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import org.junit.Test

class ShoppingFlowTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    private val login = "shopping-test-${System.nanoTime()}"

    @After
    fun removeTestData() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("local_accounts", Context.MODE_PRIVATE).edit().remove(login).commit()
        context.getSharedPreferences("demo_profile", Context.MODE_PRIVATE).edit()
            .remove("$login:name").remove("$login:avatar").commit()
    }

    @Test
    fun loginSearchCartCategoriesProfileAndRotation() {
        compose.onNodeWithText("Авторизация").assertIsDisplayed()
        compose.onNodeWithText("Тест").assertDoesNotExist()
        compose.onNodeWithText("Тест1").assertDoesNotExist()
        compose.onNodeWithText("Зарегистрироваться").performClick()
        compose.onNodeWithText("Регистрация").assertIsDisplayed()
        compose.onNodeWithText("Логин").performTextInput(login)
        compose.onNodeWithText("Пароль").performTextInput("V")
        compose.onNodeWithText("Повторите пароль").performTextInput("V")
        compose.onNodeWithText("Зарегистрироваться").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Авторизация").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Логин").performTextReplacement(login)
        compose.onNodeWithText("Пароль").performTextInput("V")
        compose.onNodeWithText("Войти").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Найти товары").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Найти товары").performTextInput("Наушники")
        compose.onNode(hasText("Наушники") and !hasSetTextAction()).assertIsDisplayed()
        compose.onNodeWithText("В корзину").performClick()
        compose.onNodeWithText("Корзина (1)").performClick()
        compose.onNodeWithText("Итого:", substring = true).assertIsDisplayed()
        compose.onNodeWithContentDescription("Увеличить количество").performClick()
        compose.onNodeWithText("2", useUnmergedTree = true).assertIsDisplayed()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("2", useUnmergedTree = true).assertIsDisplayed()
        compose.onNodeWithText("Наушники").performClick()
        compose.onNodeWithText("Характеристики").assertExists()
        compose.onNodeWithText("Назад").performClick()
        compose.onNodeWithText("Корзина", substring = false).assertIsDisplayed()
        compose.onNodeWithText("Каталог").performClick()
        compose.onNodeWithText("Книги").performClick()
        compose.onNodeWithText("Любимая книга").assertIsDisplayed()
        compose.onNodeWithText("Наушники").assertDoesNotExist()
        compose.onNodeWithText("Профиль").performClick()
        compose.onNodeWithText("Аккаунт • $login").assertIsDisplayed()
        compose.onNodeWithText("Ваше имя").performTextReplacement("Тестовый покупатель")
        compose.onNodeWithContentDescription("Лапка").performClick()
        compose.onNodeWithText("Сохранить изменения").performScrollTo().performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("Профиль сохранён на устройстве").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Выйти из аккаунта").performScrollTo().performClick()
        compose.onNodeWithText("Авторизация").assertIsDisplayed()
    }
}
