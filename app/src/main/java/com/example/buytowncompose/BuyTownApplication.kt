package com.example.buytowncompose

import android.app.Application
import com.example.buytowncompose.di.AppContainer

class BuyTownApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
