package com.example.aksharadeepatutor.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.aksharadeepatutor.ui.screens.DashboardScreen
import com.example.aksharadeepatutor.ui.screens.GapAnalysisScreen
import com.example.aksharadeepatutor.ui.screens.GoalScreen
import com.example.aksharadeepatutor.ui.screens.NotesScreen
import com.example.aksharadeepatutor.ui.screens.QuizScreen
import com.example.aksharadeepatutor.ui.screens.ReviewScreen
import com.example.aksharadeepatutor.ui.screens.StrengthScreen
import com.example.aksharadeepatutor.ui.screens.SyllabusScreen
import com.example.aksharadeepatutor.viewmodel.DashboardViewModel
import com.example.aksharadeepatutor.viewmodel.GoalViewModel
import com.example.aksharadeepatutor.viewmodel.QuizViewModel
import com.example.aksharadeepatutor.viewmodel.SyllabusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorNavGraph() {
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route ?: Screen.Dashboard.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(titleFor(currentRoute))
                },
                navigationIcon = {
                    if (currentRoute != Screen.Dashboard.route) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                val viewModel: DashboardViewModel = hiltViewModel()

                DashboardScreen(
                    viewModel = viewModel,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(Screen.Notes.route) {
                NotesScreen()
            }

            composable(Screen.Syllabus.route) {
                val viewModel: SyllabusViewModel = hiltViewModel()

                SyllabusScreen(
                    viewModel = viewModel
                )
            }

            composable(Screen.Quiz.route) {
                val viewModel: QuizViewModel = hiltViewModel()

                QuizScreen(
                    viewModel = viewModel,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(Screen.Review.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.Quiz.route)
                }

                val viewModel: QuizViewModel = hiltViewModel(parentEntry)

                ReviewScreen(
                    viewModel = viewModel
                )
            }

            composable(Screen.Strength.route) {
                val viewModel: DashboardViewModel = hiltViewModel()

                StrengthScreen(
                    viewModel = viewModel
                )
            }

            composable(Screen.Goal.route) {
                val viewModel: GoalViewModel = hiltViewModel()

                GoalScreen(
                    viewModel = viewModel
                )
            }

            composable(Screen.Gap.route) {
                val viewModel: DashboardViewModel = hiltViewModel()

                GapAnalysisScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}

private fun titleFor(route: String): String {
    return when (route) {
        Screen.Notes.route -> "Study Notes"
        Screen.Syllabus.route -> "Syllabus"
        Screen.Quiz.route -> "Quiz"
        Screen.Review.route -> "Review"
        Screen.Strength.route -> "Strength"
        Screen.Goal.route -> "Daily Goal"
        Screen.Gap.route -> "Gap Analysis"
        else -> "Akshara-Deepa"
    }
}
