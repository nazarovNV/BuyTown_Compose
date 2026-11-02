package com.example.buytowncompose.presentation

import androidx.test.platform.app.InstrumentationRegistry
import com.example.buytowncompose.domain.model.*
import com.example.buytowncompose.domain.repository.*
import com.example.buytowncompose.presentation.auth.AuthViewModel
import com.example.buytowncompose.presentation.profile.ProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Test

class FeatureViewModelTest {
    private class FakeAuth : AuthRepository {
        override val user = MutableStateFlow<User?>(null)
        val accounts = mutableMapOf<String, String>()
        var registrations = 0
        override suspend fun register(login: String, password: String): RegistrationResult {
            registrations++
            if (accounts.containsKey(login)) return RegistrationResult.LOGIN_TAKEN
            accounts[login] = password
            return RegistrationResult.SUCCESS
        }
        override suspend fun login(login: String, password: String): Boolean {
            val valid = accounts[login] == password
            if (valid) user.value = User(login)
            return valid
        }
        override fun logout() { user.value = null }
    }

    @Test
    fun registrationValidatesRepeatThenAllowsLoginWithNewCredentials() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val repository = FakeAuth()
            val vm = AuthViewModel(repository)
            vm.switchMode()
            vm.loginChanged("V")
            vm.passwordChanged("V")
            vm.repeatChanged("wrong")
            vm.submit()
            assertTrue(vm.state.value.repeatError)
            assertEquals(0, repository.registrations)
            vm.repeatChanged("V")
            vm.submit()
            assertFalse(vm.state.value.isRegistration)
            assertEquals("V", vm.state.value.login)
            assertNotNull(vm.state.value.message)
            assertEquals("", vm.state.value.password)
            vm.passwordChanged("wrong")
            vm.submit()
            assertNotNull(vm.state.value.error)
            vm.passwordChanged("V")
            vm.submit()
            assertEquals(User("V"), repository.user.value)
            assertEquals("", vm.state.value.password)
        }
    }

    @Test
    fun profileRejectsBlankNamesAndSavesTrimmedNameAndAvatar() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            var persisted = Profile("V", 0)
            var saves = 0
            val repository = object : ProfileRepository {
                override suspend fun load(login: String) = persisted
                override suspend fun save(login: String, profile: Profile): Boolean {
                    saves++
                    persisted = profile
                    return true
                }
            }
            val vm = ProfileViewModel("V", repository)
            assertEquals("V", vm.state.value.name)
            vm.nameChanged(" ")
            vm.save()
            assertTrue(vm.state.value.nameError)
            assertEquals(0, saves)
            vm.nameChanged("  Виктор  ")
            vm.avatarChanged(2)
            vm.save()
            assertEquals(Profile("Виктор", 2), persisted)
            assertTrue(vm.state.value.saved)
        }
    }
}
