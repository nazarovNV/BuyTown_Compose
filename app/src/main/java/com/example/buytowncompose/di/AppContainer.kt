package com.example.buytowncompose.di

import android.content.Context
import com.example.buytowncompose.data.source.LocalAccounts
import com.example.buytowncompose.data.repository.*
import com.example.buytowncompose.domain.repository.*

/** Application-scoped dependencies; features depend only on repository contracts. */
class AppContainer(context: Context) {
    val authRepository: AuthRepository = LocalAuthRepository(LocalAccounts(context))
    val productRepository: ProductRepository = DemoProductRepository()
    val cartRepository: CartRepository = InMemoryCartRepository(productRepository)
    val profileRepository: ProfileRepository = LocalProfileRepository(context)
}
