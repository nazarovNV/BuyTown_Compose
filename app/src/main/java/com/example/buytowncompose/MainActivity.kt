package com.example.buytowncompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buytowncompose.presentation.theme.BuyTownTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent() {
    val SampleData: List<Message> = listOf(
        Message("Коля", "Привет"),
        Message("Коля", "Как дела?"),
        Message("Коля", "Что нового?"),
        Message("Коля", "Скинь задачу номер 12004120401023012301230102"),
        Message("Коля", "Ты ее сделал? Мне нужно тестировать"),
        Message("Коля", "Что нового?"),
        Message("Коля", "Что нового?"),
        Message("Коля", "Привет"),
        Message("Коля", "Как дела?"),
        Message("Коля", "Что нового?"),
        Message("Коля", "Что нового?"),
        Message("Коля", "Привет"),
        Message("Коля", "Как дела?"),
        Message("Коля", "Что нового?")
    )
    Conversation(SampleData)


}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    BuyTownTheme(content = {
        Row(modifier = Modifier.padding(all = 18.dp)) {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Аватарка",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(20.dp))
            var isExpanded by remember { mutableStateOf(false) }

            Column {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                ) {
                    Text(
                        text = msg.body,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .clickable{isExpanded = !isExpanded },
                        // If the message is expanded, we display all its content
                        // otherwise we only display the first line
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    })

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MainContent()
}


@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}




