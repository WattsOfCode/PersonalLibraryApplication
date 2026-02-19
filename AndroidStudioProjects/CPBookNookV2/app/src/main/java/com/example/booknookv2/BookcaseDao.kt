package com.example.booknookv2

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookcaseDao {
    @Query("SELECT * FROM bookcases WHERE ownerId = :userId ORDER BY shelfName ASC")
    fun getBookcasesForUser(userId: Int): Flow<List<Bookcase>>

    @Query("SELECT * FROM bookcases WHERE ownerId = :userId")
    fun getAllBookcases(userId: Int): Flow<List<Bookcase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookcase: Bookcase)


    @Delete
    suspend fun deleteBookcase(bookcase: Bookcase)
}