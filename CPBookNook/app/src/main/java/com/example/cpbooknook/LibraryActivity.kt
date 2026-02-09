package com.example.cpbooknook
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cpbooknook.databinding.ActivityLibraryBinding
import android.view.inputmethod.EditorInfo
/** *****************************************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * Activity responsible for displaying the list of books in the user's library.
 * It uses the BookController to retrieve the data.
 ****************************************************************************** */
class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var controller: BookController
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Controller instance from the Application
        val application = application as BookNookApplication
        controller = application.bookController

        // listeners
        setupRecyclerView()
        setupSearchListeners()
        setupLogoAsBackButton()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list every time the user comes back (after adding/editing)
        val currentQuery = binding.editSearchInput.text.toString()
        performSearch(currentQuery)
    }

    private fun setupRecyclerView() {
        // Initialize the Adapter (with an empty list for now)
        bookAdapter = BookAdapter(emptyList()) { book ->
            handleBookClick(book)
        }

        // Configure the RecyclerView
        binding.recyclerViewLibrary.apply {
            layoutManager = LinearLayoutManager(this@LibraryActivity)
            adapter = bookAdapter
        }
    }

    // --- Search implementation ---
    private fun setupSearchListeners() {
        //listener for search button click
        binding.btnSearch.setOnClickListener {
            val query = binding.editSearchInput.text.toString()
            performSearch(query)        }


    //listener for hte action search key press
        binding.editSearchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString()
                performSearch(query)
                // Consume the event (return true) so the keyboard doesn't process it further
                true
            } else {
                false
            }
        }
    }

    // performs search using book controller and updates recycler view
    private fun performSearch(query: String) {
        val filteredBooks = controller.searchBooks(query)
        bookAdapter.updateList(filteredBooks)
        hideKeyboard()
    }

    //  Fetches the current list of books from the Controller and updates the Adapter.
    private fun loadBooks() {
        val books = controller.getLibraryBooks()
        bookAdapter.updateList(books)
    }

    private fun setupLogoAsBackButton() {
        binding.imgLogoBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    //helper function to hide keyboard
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun handleBookClick(book: Book) {
        val intent = Intent(this, EditActivity::class.java).apply {
        putExtra("BOOK_ID", book.id)
    }
        startActivity(intent)
    }
}