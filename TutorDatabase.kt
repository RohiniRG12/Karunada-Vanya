package com.example.aksharadeepatutor.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aksharadeepatutor.data.dao.TutorDao
import com.example.aksharadeepatutor.data.entity.*

@Database(
    entities = [
        QuestionEntity::class,
        ChapterProgressEntity::class,
        QuizAttemptEntity::class,
        DailyGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TutorDatabase : RoomDatabase() {
    abstract fun tutorDao(): TutorDao
}
