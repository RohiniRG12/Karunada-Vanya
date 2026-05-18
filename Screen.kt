package com.example.aksharadeepatutor.navigation


sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Notes : Screen("notes")
    data object Syllabus : Screen("syllabus")
    data object Quiz : Screen("quiz")
    data object Review : Screen("review")
    data object Strength : Screen("strength")
    data object Goal : Screen("goal")
    data object Gap : Screen("gap")
}
