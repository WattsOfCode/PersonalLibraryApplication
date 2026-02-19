package com.example.booknookv2

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = BookRepository(db.bookDao(), db.bookcaseDao())
    }
    fun getAllBookcases(userId: Int): LiveData<List<Bookcase>> {
        return repository.getAllBookcases(userId)
    }
    fun getBooksForUser(userId: Int): LiveData<List<Book>> { return repository.getBooksForUser(userId).asLiveData() }
    fun addBook(book: Book) = viewModelScope.launch { repository.addBook(book) }
    fun addBookcase(bookcase: Bookcase) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBookcase(bookcase)
        }
    }

    fun searchBooks(userId: Int, query: String): LiveData<List<Book>> { return repository.searchUserLibrary(userId, "%$query%").asLiveData() }

}