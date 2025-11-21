package com.example.hans_on_assignment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlashcardDao {

    @Insert
    suspend fun insert(flashcard: Flashcard)

    @Query("SELECT DISTINCT category FROM flashcards ORDER BY category")
    suspend fun getDistinctCategories(): List<String>

    @Query("SELECT * FROM flashcards WHERE category = :category")
    suspend fun getByCategory(category: String): List<Flashcard>
    @Query("DELETE FROM flashcards")
    suspend fun deleteAll()

}
