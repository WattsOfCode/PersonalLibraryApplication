package com.example.booknookv2

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books WHERE ownerId = :userId")
    fun getBooksForUser(userId: Int): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE ownerId = :userId AND bookcaseId = :shelfId")
    fun getBooksByShelf(userId: Int, shelfId: Int): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE ownerId = :userId AND isLoaned = 1")
    fun getLoanedBooks(userId: Int): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE ownerId = :userId AND (title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%')")
    fun searchUserLibrary(userId: Int, query: String): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)
    @Delete
    suspend fun deleteBook(book: Book)
}