package com.example.buytowncompose.ui

import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.buytowncompose.presentation.auth.AuthScreen
import com.example.buytowncompose.presentation.auth.AuthUiState
import com.example.buytowncompose.presentation.common.CartQuantityControl
import com.example.buytowncompose.presentation.profile.ProfileScreen
import com.example.buytowncompose.presentation.profile.ProfileUiState
import com.example.buytowncompose.presentation.theme.BuyTownTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** States that are too brief or require storage failures to reproduce reliably end to end. */
class ScreenStateUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun authLoadingPreventsRepeatedSubmissionsAndEditing() {
        compose.setContent {
            BuyTownTheme {
                AuthScreen(AuthUiState(isRegistration = true, isLoading = true), {}, {}, {}, {}, {})
            }
        }
        listOf("auth.login", "auth.password", "auth.repeat", "auth.submit", "auth.switch").forEach {
            compose.onNodeWithTag(it).assertIsNotEnabled()
        }
        compose.onNodeWithText("Подождите…").assertExists()
    }

    @Test fun profileLoadingPreventsEditingAndSave() {
        compose.setContent {
            BuyTownTheme { ProfileScreen(ProfileUiState("V", isLoading = true), {}, {}, {}, {}, {}) }
        }
        compose.onNodeWithTag("profile.name").assertIsNotEnabled()
        compose.onNodeWithTag("profile.save").assertIsNotEnabled()
        (0..3).forEach { compose.onNodeWithTag("profile.avatar:$it").assertIsNotEnabled() }
    }

    @Test fun profileStorageErrorShowsRetryAndInvokesCallback() {
        var retries = 0
        compose.setContent {
            BuyTownTheme {
                ProfileScreen(ProfileUiState("V", isLoading = false, error = "Не удалось загрузить профиль"),
                    {}, {}, {}, { retries++ }, {})
            }
        }
        compose.onNodeWithText("Не удалось загрузить профиль").assertExists()
        compose.onNodeWithText("Загрузить заново").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, retries) }
    }

    @Test fun counterDisablesPlusAt99AndEnablesItAfterDecrement() {
        compose.setContent {
            var quantity by remember { mutableIntStateOf(98) }
            BuyTownTheme { CartQuantityControl(quantity, { quantity-- }, { quantity++ }) }
        }
        compose.onNodeWithContentDescription("Увеличить количество").performClick()
        compose.onNodeWithText("99").assertExists()
        compose.onNodeWithContentDescription("Увеличить количество").assertIsNotEnabled()
        compose.onNodeWithContentDescription("Уменьшить количество").performClick()
        compose.onNodeWithText("98").assertExists()
        compose.onNodeWithContentDescription("Увеличить количество").assertIsEnabled()
    }
}
