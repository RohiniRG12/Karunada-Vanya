package com.example.aksharadeepatutor.repository

import com.example.aksharadeepatutor.data.dao.TutorDao
import com.example.aksharadeepatutor.data.entity.ChapterProgressEntity
import com.example.aksharadeepatutor.data.entity.DailyGoalEntity
import com.example.aksharadeepatutor.data.entity.QuestionEntity
import com.example.aksharadeepatutor.data.entity.QuizAttemptEntity
import com.example.aksharadeepatutor.data.seed.SeedData
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TutorRepository @Inject constructor(
    private val dao: TutorDao
) {
    val subjects: List<String> = SeedData.subjects
    val chapters: Map<String, List<String>> = SeedData.chapters

    suspend fun seedIfNeeded() {
        if (dao.questionCount() == 0) {
            dao.insertQuestions(SeedData.questions)
        }

        if (dao.chapterCount() == 0) {
            dao.insertChapters(SeedData.chapterProgress)
        }
    }

    fun observeChapterProgress(): Flow<List<ChapterProgressEntity>> {
        return dao.observeChapterProgress()
    }

    fun observeQuizAttempts(): Flow<List<QuizAttemptEntity>> {
        return dao.observeQuizAttempts()
    }

    fun observeDailyGoal(date: String): Flow<DailyGoalEntity?> {
        return dao.observeDailyGoal(date)
    }

    suspend fun getQuestions(subject: String): List<QuestionEntity> {
        return dao.getQuestionsBySubject(subject)
    }

    suspend fun updateChapter(progress: ChapterProgressEntity) {
        dao.updateChapter(progress)
    }

    suspend fun saveQuizAttempt(subject: String, score: Int, total: Int) {
        dao.insertQuizAttempt(
            QuizAttemptEntity(
                subject = subject,
                score = score,
                total = total
            )
        )
    }

    suspend fun setDailyStudiedTopic(date: String, value: Boolean) {
        updateDailyGoal(date, studiedTopic = value, completedQuiz = null)
    }

    suspend fun setDailyCompletedQuiz(date: String, value: Boolean) {
        updateDailyGoal(date, studiedTopic = null, completedQuiz = value)
    }

    private suspend fun updateDailyGoal(
        date: String,
        studiedTopic: Boolean?,
        completedQuiz: Boolean?
    ) {
        val existing = dao.getDailyGoal(date)
        val yesterday = yesterdayOf(date)
        val yesterdayGoal = dao.getDailyGoal(yesterday)

        val newStudied = studiedTopic ?: existing?.studiedTopic ?: false
        val newQuiz = completedQuiz ?: existing?.completedQuiz ?: false
        val completeToday = newStudied && newQuiz

        val streak = when {
            !completeToday -> existing?.streak ?: 0
            (existing?.streak ?: 0) > 0 -> existing?.streak ?: 1
            yesterdayGoal?.studiedTopic == true && yesterdayGoal.completedQuiz -> yesterdayGoal.streak + 1
            else -> 1
        }

        dao.upsertDailyGoal(
            DailyGoalEntity(
                date = date,
                studiedTopic = newStudied,
                completedQuiz = newQuiz,
                streak = streak
            )
        )
    }

    fun today(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
    }

    private fun yesterdayOf(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(date) ?: Calendar.getInstance().time
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return formatter.format(calendar.time)
    }
}
