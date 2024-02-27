package com.example.carakinatormobile

import Car
import CarRepositoryClass
import Question
import QuestionRepositoryClass
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carakinatormobile.ui.theme.CarAkinatorMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarAkinatorMobileTheme {
                Application()
            }
        }
    }
}
@Composable
@Preview
fun Application() {
    val car = CarRepositoryClass()
    val filteredCars = remember { mutableStateOf(car.getCar()) }
    val currentQuestion = remember { mutableStateOf(SelectNewQuestion.getNextQuestion()) }
    val question = currentQuestion.value

    if (question != null) {
        questionItem(question, onAnswer = { answer ->
            filteredCars.value = filteredCars.value.filterByAnswer(question, answer)
            currentQuestion.value = SelectNewQuestion.getNextQuestion()
        })
    } else {
        val yourCar = filteredCars.value
        when (yourCar.size) {
            0 -> Text(text = "Вам не подходит ни одна существующая машина")
            1 -> Text(text = "Ваша машина ${yourCar.single().name}")
            else -> Text(
                text = "Вам подходят следующие авто: ${
                    yourCar.joinToString(
                        prefix = "Вам подходят авто: ",
                        transform = { car -> car.name })
                }"
            )
        }
    }
}

object SelectNewQuestion {
    val question = QuestionRepositoryClass().getQuestion().toMutableList()

    fun getNextQuestion(): Question? {
        if (question.isEmpty())
            return null
        else {
            val randomQuestion = question.random()
            question.remove(randomQuestion)
            return randomQuestion
        }
    }
}

fun List<Car>.filterByAnswer(currentQuestion: Question, answer: Boolean) =
    filter { car -> currentQuestion.checkCondition(answer, car) }

@Composable
fun questionItem(question: Question, onAnswer: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(bottom = 35.dp), verticalArrangement = Arrangement.Bottom) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = {
                onAnswer(true)
            }
            ) {
                Text(text = "Да", fontSize = 32.sp)
            }
            TextButton(onClick = {
                onAnswer(false)
            }) {
                Text(text = "Нет", fontSize = 32.sp)
            }

        }
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = question.description())
        }
    }

}