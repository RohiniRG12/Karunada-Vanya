package com.example.aksharadeepatutor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aksharadeepatutor.data.seed.SeedData
import com.example.aksharadeepatutor.ui.components.TutorCard

@Composable
fun NotesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Study Notes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Read short chapter notes before attempting quizzes.")

        SeedData.notes.groupBy { it.subject }.forEach { entry ->
            Text(
                text = entry.key,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            entry.value.forEach { note ->
                TutorCard(
                    title = note.chapter,
                    subtitle = note.content,
                    accent = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
