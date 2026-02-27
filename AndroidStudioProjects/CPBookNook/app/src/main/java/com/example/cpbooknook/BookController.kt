package com.example.cpbooknook
/** *****************************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * The primary Controller for the application.
 * It sits between the View (Fragments) and the Model (BookManager).
 * All UI actions are routed through here.
 ******************************************************************* */
class BookController(private val bookManager: BookManager) {

    // --- Data Access (Reads) ---
    // Expose the list of books directly from the Manager
    fun getLibraryBooks(): List<Book> { return bookManager.getAllBooks() }

    // --- Action Handlers (Writes) ---
    // Handles the user action to save a new book.
    fun addBook(book: Book) { bookManager.addBook(book) }

    // Handles updating an existing book.
    fun updateBook(book: Book) { bookManager.updateBook(book) }

    // Handles deleting a book.
    fun deleteBook(book: Book) { bookManager.deleteBook(book) }

    fun searchBooks(query: String): List<Book> {
        // Delegates the actual search logic to the book manager
        return bookManager.searchBooks(query)
    }
    //for the bookbyid functions to be called in the edit activity file
    fun getBookById(id: Int): Book? {
        return bookManager.getAllBooks().find { it.id == id }    }

}