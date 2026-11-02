package com.example.buytowncompose.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.buytowncompose.presentation.theme.BuyTownTheme

@Composable
fun CustomText(text: String) {
    BuyTownTheme{
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth()
        )
    }

}