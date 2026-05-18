package com.example.aksharadeepatutor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aksharadeepatutor.navigation.Screen
import com.example.aksharadeepatutor.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    navigate: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Practice Quiz",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Each quiz has 5 questions. Questions do not repeat until the bank is completed.")

        if (!state.isRunning && !state.isFinished) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                state.subjects.forEach { subject ->
                    FilterChip(
                        selected = state.selectedSubject == subject,
                        onClick = { viewModel.selectSubject(subject) },
                        label = { Text(subject) }
                    )
                }
            }

            Button(
                onClick = { viewModel.startQuiz() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Quiz")
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        if (state.isRunning) {
            val question = state.questions[state.currentIndex]

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Question ${state.currentIndex + 1}/${state.questions.size}")
                Text(
                    text = "${state.secondsLeft}s",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            LinearProgressIndicator(
                progress = { state.secondsLeft / 30f },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = question.question,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            question.options.forEach { option ->
                OutlinedButton(
                    onClick = { viewModel.answer(option) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(option)
                }
            }
        }

        if (state.isFinished) {
            val percent = if (state.questions.isEmpty()) {
                0
            } else {
                state.score * 100 / state.questions.size
            }

            Text(
                text = "Quiz Completed",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Score: ${state.score}/${state.questions.size} ($percent%)",
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = { navigate(Screen.Review.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Results")
            }

            OutlinedButton(
                onClick = { viewModel.startQuiz() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Take Another Quiz")
            }
        }
    }
}
