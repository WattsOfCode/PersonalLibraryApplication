package com.example.cpbooknook
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
/** *********************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * Manages the actual list of books. It handles
 * persistent storage (loading and saving the book data
 * to the JSON file), manual ID generation, and providing
 * methods for retrieving and filtering the book collection.
 ***********************************************************  */
class BookManager(private val context: Context) {

    //file name for storing the book data
    private val FILENAME = "book_nook_data.json"
    private val gson = Gson()
    //list to hold the books in memory for fast access

    private var books: MutableList<Book> = loadBooks()

    // --- Public Access Methods (The "DAO" / "Repository") ---
    // Returns a copy of the in-memory list
    fun getAllBooks(): List<Book> {
        return books.toList()
    }

    // Adds a new book to the list and saves it to the file.
    fun addBook(book: Book) {
        val maxId = books.maxOfOrNull { it.id } ?: 0
        val newId = maxId + 1
        val newBook = book.copy(id = newId)

        books.add(newBook)
        saveBooks()
    }

    // Updates an existing book and saves the changes.
    fun updateBook(updatedBook: Book) {
        val index = books.indexOfFirst { it.id == updatedBook.id }
        if (index != -1) {
            books[index] = updatedBook
            saveBooks()
        }
    }

    //Deletes a book and saves the changes.
    fun deleteBook(book: Book) {
        books.removeIf { it.id == book.id }
        saveBooks()
    }

    // --- Private File I/O Methods ---
    // Reads the book list from the internal storage file.
         private fun loadBooks(): MutableList<Book> {
        val file = File(context.filesDir, FILENAME)
        if (!file.exists()) return mutableListOf()

        return try {
            FileReader(file).use { reader ->
                // Gson needs a TypeToken to know how to deserialize a List of objects
                val type = object : TypeToken<MutableList<Book>>() {}.type
                gson.fromJson<MutableList<Book>>(reader, type) ?: mutableListOf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }
    // Writes the current in-memory book list to the internal storage file.
    private fun saveBooks() {
        val file = File(context.filesDir, FILENAME)
        try {
            FileWriter(file).use { writer ->
                gson.toJson(books, writer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun searchBooks(query: String): List<Book> {
        if (query.isBlank()) {
            return books.toList()
        }

        val lowerCaseQuery = query.trim().lowercase()

        return books.filter { book ->
            book.title.orEmpty().lowercase().contains(lowerCaseQuery) ||
            book.author.orEmpty().lowercase().contains(lowerCaseQuery) ||
            book.tagsGenre.orEmpty().lowercase().contains(lowerCaseQuery) ||
            book.publisher.orEmpty().lowercase().contains(lowerCaseQuery) ||
            book.isbn.orEmpty().contains(lowerCaseQuery)
        }.toList()
    }
}