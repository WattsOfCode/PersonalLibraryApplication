package com.example.booknookv2

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class BookRepository(
    private val bookDao: BookDao,
    private val bookcaseDao: BookcaseDao) {

    fun getBooksForUser(userId: Int): Flow<List<Book>> = bookDao.getBooksForUser(userId)
    suspend fun addBook(book: Book) { bookDao.insertBook(book) }
    suspend fun deleteBook(book: Book) { bookDao.deleteBook(book) }

    fun searchUserLibrary(userId: Int, query: String): Flow<List<Book>> {
        return bookDao.searchUserLibrary(userId, query)
    }

    fun getAllBookcases(userId: Int): LiveData<List<Bookcase>> {
        return bookcaseDao.getAllBookcases(userId).asLiveData()
    }

    suspend fun insertBookcase(bookcase: Bookcase) {
        bookcaseDao.insert(bookcase)
    }
}