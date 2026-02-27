package com.example.booknookv2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

class BookRepository(
    private val bookDao: BookDao,
    private val bookcaseDao: BookcaseDao) {

    private val googleApi = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleBooksService::class.java)

    private val olApi = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenLibraryService::class.java)

    suspend fun fetchBookFromWeb(isbn: String, userId: Int): Book? {
        val cleanIsbn = isbn.trim().replace("-", "").replace(" ", "")
        val googleBook = try {
            val response = googleApi.searchByIsbn("isbn:$cleanIsbn")
            val info = response.items?.firstOrNull()?.volumeInfo
            if (info != null) {
                Book(
                    ownerId = userId,
                    bookcaseId = null,
                    title = info.title,
                    author = info.authors?.joinToString(", ") ?: "Unknown Author",
                    isbn = cleanIsbn,
                    summary = info.description,
                    status = "Owned"
                )
            } else null
        } catch (e: Exception) { null }

        if (googleBook != null) return googleBook

        return try {
            val olResponse = olApi.getBookByIsbn("ISBN:$cleanIsbn")
            val olData = olResponse["ISBN:$cleanIsbn"]

            if (olData != null) {
                val authorName = olData.details?.authors?.firstOrNull()?.name ?: "Indie Author"

                Book(
                    ownerId = userId,
                    bookcaseId = null,
                    title = olData.details?.title ?: "Unknown Title",
                    author = authorName, // OL authors are nested deep, placeholder for now
                    isbn = cleanIsbn,
                    pageCount = olData.details?.number_of_pages,
                    status = "Owned",
                    imageUrl = olData.thumbnail_url
                )
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
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