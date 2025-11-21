package com.example.flashcards.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert
    suspend fun insert(flashcard: Flashcard)

    @Query("SELECT DISTINCT category FROM flashcards ORDER BY category")
    fun getDistinctCategories(): Flow<List<String>>

    @Query("""
        SELECT * FROM flashcards
        WHERE category = :category
        ORDER BY RANDOM()
        LIMIT 1
    """)
    suspend fun getRandomByCategory(category: String): Flashcard?
}
