package com.example.buytowncompose.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.presentation.common.CustomButton
import com.example.buytowncompose.presentation.common.CustomInput
import com.example.buytowncompose.presentation.common.CustomText

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .padding(all = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        CustomText(text = "Добро пожаловать в приложение BuyTownCompose")
        CustomText(text = "Войдите в аккаунт:")

        CustomInput(label = "Введите логин")
        CustomInput(label = "Введите пароль")
        CustomButton(buttonText = "Войти")
        CustomText(text = "Еще нет аккаунта? Зарегистрируйтесь:")
        CustomButton(buttonText = "Регистрация")
    }


}


@Preview(showBackground = true)
@Composable
fun Preview() {
    LoginScreen()
}