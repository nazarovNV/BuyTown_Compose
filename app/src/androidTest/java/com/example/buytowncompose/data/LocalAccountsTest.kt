package com.example.buytowncompose.data

import com.example.buytowncompose.data.source.LocalAccounts
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

class LocalAccountsTest {
    @Test
    fun registeredAccountSurvivesRecreationAndRejectsIncorrectPassword() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val login = "V-${System.nanoTime()}"
        try {
            assertTrue(LocalAccounts(context).register(login, "V"))
            val reopenedAccounts = LocalAccounts(context)
            assertTrue(reopenedAccounts.authenticate(login, "V"))
            assertFalse(reopenedAccounts.authenticate(login, "wrong"))
            assertFalse(reopenedAccounts.register(login, "replacement"))
            assertTrue(reopenedAccounts.authenticate(login, "V"))
            assertTrue(reopenedAccounts.authenticate("demo", "demo123"))
            assertFalse(reopenedAccounts.register("demo", "replacement"))
        } finally {
            context.getSharedPreferences("local_accounts", Context.MODE_PRIVATE).edit().remove(login).commit()
        }
    }
}
