package com.example.buytowncompose.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.buytowncompose.presentation.theme.BuyTownTheme

@Composable
fun CustomButton (buttonText: String) {
    BuyTownTheme(content = {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { /* Действие при нажатии */ }
        ) {
            Text(text = buttonText)
        }
    })

}