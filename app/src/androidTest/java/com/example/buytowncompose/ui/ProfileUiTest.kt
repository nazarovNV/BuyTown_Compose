package com.example.buytowncompose.ui

import androidx.compose.ui.test.*
import org.junit.Test

class ProfileUiTest : BaseUiTest() {
    @Test fun blankNameIsRejected() {
        login()
        tapTag("tab:PROFILE")
        compose.waitUntil(15_000) { compose.onAllNodes(hasTestTag("profile.name") and isEnabled()).fetchSemanticsNodes().isNotEmpty() }
        fill("profile.name", " ")
        tapTag("profile.save", true)
        compose.onNodeWithText("Введите имя").assertExists()
        compose.onNodeWithText("Профиль сохранён на устройстве").assertDoesNotExist()
    }

    @Test fun nameAndAvatarPersistAfterLogoutAndLogin() {
        val account = login()
        tapTag("tab:PROFILE")
        compose.waitUntil(15_000) { compose.onAllNodes(hasTestTag("profile.name") and isEnabled()).fetchSemanticsNodes().isNotEmpty() }
        fill("profile.name", "  Виктор  ")
        tapTag("profile.avatar:2")
        tapTag("profile.save", true)
        waitText("Профиль сохранён на устройстве")
        logout()
        login(account)
        tapTag("tab:PROFILE")
        compose.waitUntil(15_000) { compose.onAllNodes(hasTestTag("profile.name") and isEnabled()).fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithTag("profile.name").assertTextContains("Виктор")
        compose.onNodeWithTag("profile.avatar:2").assertIsOn()
    }

    @Test fun draftSurvivesRotationAndAllAvatarsCanBeSelected() {
        login()
        tapTag("tab:PROFILE")
        compose.waitUntil(15_000) { compose.onAllNodes(hasTestTag("profile.name") and isEnabled()).fetchSemanticsNodes().isNotEmpty() }
        fill("profile.name", "Черновик")
        (0..3).forEach { index ->
            tapTag("profile.avatar:$index")
            compose.onNodeWithTag("profile.avatar:$index").assertIsOn()
        }
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("profile.name").assertTextContains("Черновик")
        compose.onNodeWithTag("profile.avatar:3").assertIsOn()
    }

    @Test fun anotherAccountHasItsOwnProfile() {
        login()
        tapTag("tab:PROFILE")
        compose.waitUntil(15_000) { compose.onAllNodes(hasTestTag("profile.name") and isEnabled()).fetchSemanticsNodes().isNotEmpty() }
        fill("profile.name", "Имя первого пользователя")
        tapTag("profile.avatar:1")
        tapTag("profile.save", true)
        waitText("Профиль сохранён на устройстве")
        logout()
        val second = login()
        tapTag("tab:PROFILE")
        compose.waitUntil(15_000) { compose.onAllNodes(hasTestTag("profile.name") and isEnabled()).fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithTag("profile.name").assertTextContains(second)
        compose.onNodeWithTag("profile.avatar:0").assertIsOn()
    }
}
