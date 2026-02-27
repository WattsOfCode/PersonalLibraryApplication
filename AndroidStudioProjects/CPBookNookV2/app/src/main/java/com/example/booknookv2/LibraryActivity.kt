package com.example.booknookv2

import android.content.Intent
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

        bookAdapter = BookAdapter(emptyList()) { selectedBook ->
            val intent = Intent(this, EditBookActivity::class.java)
            intent.putExtra("BOOK_ID", selectedBook.id)
            startActivity(intent)
        }
        binding.recyclerViewLibrary.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLibrary.adapter = bookAdapter

        val userId = UserSession.currentUser?.id ?: -1
        viewModel.getBooksForUser(userId).observe(this) { books ->
            bookAdapter.updateList(books)
        }
        binding.btnSearch.setOnClickListener {
            val query = binding.editSearchInput.text.toString()
            viewModel.searchBooks(userId, query).observe(this) { filteredBooks ->
                bookAdapter.updateList(filteredBooks)
            }
        }
    }

    private fun showAddBookcaseDialog() {
        val input = android.widget.EditText(this)
        input.hint = "e.g. living room, office, To-Read"

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("New Bookcase")
            .setMessage("Enter a name for your new bookcase:")
            .setView(input)
            .setPositiveButton("Create") { _, _ ->
                val name = input.text.toString()
                if (name.isNotBlank()) {
                    val userId = UserSession.currentUser?.id ?: -1

                    val newBookcase = Bookcase(
                        ownerId = userId,
                        shelfName = name,
                        shelfDescription = "My shelf collection"
                    )
                    viewModel.addBookcase(newBookcase)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}