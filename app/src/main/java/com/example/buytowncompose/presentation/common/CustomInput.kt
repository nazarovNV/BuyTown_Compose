package com.example.buytowncompose.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.buytowncompose.presentation.theme.BuyTownTheme

@Composable
fun CustomInput(label:String) {
    var text by remember { mutableStateOf("") }
    BuyTownTheme{
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            label = { Text(label) }
        )
    }

}