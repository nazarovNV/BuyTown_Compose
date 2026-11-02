package com.example.buytowncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.buytowncompose.di.AppContainer
import com.example.buytowncompose.presentation.navigation.AppRoute
import com.example.buytowncompose.presentation.theme.BuyTownTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as BuyTownApplication).container
        setContent { MainContent(container) }
    }
}

@Composable
fun MainContent(container: AppContainer) {
    BuyTownTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppRoute(container)
        }
    }
}
