package com.example.buytowncompose.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import com.example.buytowncompose.BuyTownApplication
import com.example.buytowncompose.MainActivity
import com.example.buytowncompose.data.source.LocalAccounts
import org.junit.After
import org.junit.Before
import org.junit.Rule

/** Real activity, real repositories, isolated accounts. No server or mock UI. */
abstract class BaseUiTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    private val accounts = mutableSetOf<String>()
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext
    private val application get() = context.applicationContext as BuyTownApplication
    private val demoDelay get() = InstrumentationRegistry.getArguments()
        .getString("demoDelayMs")?.toLongOrNull()?.coerceIn(0, 2_000) ?: 0

    @Before
    fun startLoggedOut() {
        compose.runOnIdle { application.container.authRepository.logout() }
        waitText("Авторизация")
    }

    @After
    fun cleanUp() {
        compose.runOnIdle { application.container.authRepository.logout() }
        accounts.forEach { login ->
            context.getSharedPreferences("local_accounts", Context.MODE_PRIVATE).edit().remove(login).commit()
            context.getSharedPreferences("demo_profile", Context.MODE_PRIVATE).edit()
                .remove("$login:name").remove("$login:avatar").commit()
        }
    }

    protected fun newLogin(): String = "ui-${System.nanoTime()}".also { accounts.add(it) }
    protected fun seedAccount(password: String = "V"): String = newLogin().also {
        check(LocalAccounts(context).register(it, password))
    }
    protected fun step() {
        compose.waitForIdle()
        // Optional visual playback only. Assertions use Compose synchronization and waitUntil.
        if (demoDelay > 0) Thread.sleep(demoDelay)
    }
    protected fun tapTag(tag: String, scroll: Boolean = false) {
        val node = compose.onNodeWithTag(tag)
        if (scroll) node.performScrollTo()
        node.performClick()
        step()
    }
    protected fun tapText(text: String, scroll: Boolean = false) {
        val node = compose.onNodeWithText(text)
        if (scroll) node.performScrollTo()
        node.performClick()
        step()
    }
    protected fun fill(tag: String, value: String) {
        compose.onNodeWithTag(tag).performTextReplacement(value)
        Espresso.closeSoftKeyboard()
        step()
    }
    protected fun waitText(text: String) {
        compose.waitUntil(15_000) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
        step()
    }
    protected fun login(login: String = seedAccount(), password: String = "V"): String {
        fill("auth.login", login)
        fill("auth.password", password)
        tapTag("auth.submit", scroll = true)
        compose.waitUntil(15_000) { compose.onAllNodesWithTag("home.search").fetchSemanticsNodes().isNotEmpty() }
        step()
        return login
    }
    protected fun register(login: String, password: String = "V") {
        tapTag("auth.switch", scroll = true)
        fill("auth.login", login)
        fill("auth.password", password)
        fill("auth.repeat", password)
        tapTag("auth.submit", scroll = true)
        waitText("Аккаунт создан. Введите пароль, чтобы войти.")
    }
    protected fun logout() {
        tapTag("tab:PROFILE")
        tapTag("profile.logout", scroll = true)
        waitText("Авторизация")
    }
    protected fun search(value: String) = fill("home.search", value)
    protected fun showProduct(id: Int) {
        compose.onNodeWithTag("products.grid").performScrollToNode(hasTestTag("product:$id"))
        step()
    }
    protected fun productAction(id: Int, matcher: SemanticsMatcher) = compose.onNode(
        matcher and hasAnyAncestor(hasTestTag("product:$id")))
    protected fun add(id: Int) {
        showProduct(id)
        productAction(id, hasText("В корзину")).performClick()
        step()
    }
    protected fun increase(id: Int) {
        productAction(id, hasContentDescription("Увеличить количество")).performScrollTo().performClick()
        step()
    }
    protected fun decrease(id: Int) {
        productAction(id, hasContentDescription("Уменьшить количество")).performScrollTo().performClick()
        step()
    }
    protected fun assertQuantity(id: Int, count: Int) {
        compose.onNode(hasText(count.toString()) and hasAnyAncestor(hasTestTag("product:$id")),
            useUnmergedTree = true).assertExists()
    }
    protected fun cart() = tapTag("shop.cart")
    protected fun back() {
        Espresso.pressBack()
        step()
    }
}
