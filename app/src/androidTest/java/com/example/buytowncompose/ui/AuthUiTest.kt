package com.example.buytowncompose.ui

import androidx.compose.ui.test.*
import org.junit.Test
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.text.AnnotatedString

class AuthUiTest : BaseUiTest() {
    @Test fun startsWithAuthorizationAndCanSwitchForms() {
        compose.onNodeWithText("Авторизация").assertIsDisplayed()
        compose.onNodeWithTag("auth.otter").assertIsDisplayed()
            .assertContentDescriptionEquals("Выдрёнок BuyTown бежит с пакетами покупок")
        compose.onNodeWithText("BuyTown").assertDoesNotExist()
        compose.onNodeWithText("Тест").assertDoesNotExist()
        compose.onNodeWithTag("auth.repeat").assertDoesNotExist()
        tapTag("auth.switch", true)
        compose.onNodeWithText("Регистрация").assertIsDisplayed()
        compose.onNodeWithTag("auth.otter").assertExists()
        compose.onNodeWithTag("auth.repeat").assertExists()
        tapTag("auth.switch", true)
        compose.onNodeWithText("Авторизация").assertIsDisplayed()
    }

    @Test fun emptyLoginShowsRequiredFieldErrors() {
        tapTag("auth.submit", true)
        compose.onNodeWithText("Введите логин").assertExists()
        compose.onNodeWithText("Введите пароль").assertExists()
        compose.onNodeWithTag("home.search").assertDoesNotExist()
    }

    @Test fun wrongPasswordCanBeCorrected() {
        val account = seedAccount()
        fill("auth.login", account)
        fill("auth.password", "wrong")
        tapTag("auth.submit", true)
        waitText("Неверный логин или пароль")
        login(account)
        compose.onNodeWithTag("tab:HOME").assertIsSelected()
    }

    @Test fun unknownAccountCannotLogin() {
        fill("auth.login", newLogin())
        fill("auth.password", "V")
        tapTag("auth.submit", true)
        waitText("Неверный логин или пароль")
        compose.onNodeWithTag("home.search").assertDoesNotExist()
    }

    @Test fun registrationRequiresFieldsAndMatchingPasswords() {
        tapTag("auth.switch", true)
        tapTag("auth.submit", true)
        compose.onNodeWithText("Введите логин").assertExists()
        compose.onNodeWithText("Введите пароль").assertExists()
        fill("auth.login", newLogin())
        fill("auth.password", "V")
        fill("auth.repeat", "other")
        tapTag("auth.submit", true)
        compose.onNodeWithText("Пароли не совпадают").assertExists()
        compose.onNodeWithText("Регистрация").assertExists()
    }

    @Test fun duplicateLoginDoesNotOverwriteExistingPassword() {
        val account = seedAccount("original")
        tapTag("auth.switch", true)
        fill("auth.login", account)
        fill("auth.password", "replacement")
        fill("auth.repeat", "replacement")
        tapTag("auth.submit", true)
        waitText("Этот логин уже занят")
        tapTag("auth.switch", true)
        login(account, "original")
    }

    @Test fun registeredSingleCharacterPasswordSurvivesActivityRecreationAndLogout() {
        val account = newLogin()
        register(account)
        compose.onNodeWithTag("auth.login").assertTextContains(account)
        compose.onNodeWithTag("auth.password").assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString("")))
        compose.activityRule.scenario.recreate()
        login(account)
        logout()
        login(account)
    }

    @Test fun demoAccountStillWorks() {
        login("demo", "demo123")
        compose.onNodeWithTag("tab:HOME").assertIsSelected()
        logout()
        compose.onNodeWithTag("auth.password").assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString("")))
    }

    @Test fun systemBackFromRegistrationReturnsToLogin() {
        tapTag("auth.switch", true)
        back()
        compose.onNodeWithText("Авторизация").assertIsDisplayed()
    }
}
