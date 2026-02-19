package com.example.booknookv2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booknookv2.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        // 1. Setup the Adapter
        bookAdapter = BookAdapter(emptyList()) { book ->
            // Logic for when a book is clicked (like opening a detail view)
        }

        // 2. Setup the RecyclerView (using your XML ID)
        binding.recyclerViewLibrary.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLibrary.adapter = bookAdapter

        // 3. Load all books for the logged-in user initially
        val userId = UserSession.currentUser?.id ?: -1
        viewModel.getBooksForUser(userId).observe(this) { books ->
            bookAdapter.updateList(books)
        }

        // 4. Handle Search Button
        binding.btnSearch.setOnClickListener {
            val query = binding.editSearchInput.text.toString()
            viewModel.searchBooks(userId, query).observe(this) { filteredBooks ->
                bookAdapter.updateList(filteredBooks)
            }
        }
    }
}