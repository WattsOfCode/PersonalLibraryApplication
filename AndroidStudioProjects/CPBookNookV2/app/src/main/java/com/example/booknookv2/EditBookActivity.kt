package com.example.booknookv2

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.booknookv2.databinding.ActivityEditBookBinding
import org.json.JSONObject
import java.net.URL

class EditBookActivity : AppCompatActivity() {
    private var allBookcases: List<Bookcase> = emptyList()
    private var selectedBookcaseId: Int? = null
    private var bookId: Int = -1
    private lateinit var binding: ActivityEditBookBinding
    private lateinit var viewModel: BookViewModel
    private var selectedImageUri: Uri? = null
    private var existingCoverPath: String? = null

    private var currentIsbn: String? = null
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.imgBookCoverPreview.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        SetupBookcaseSpinner()

        viewModel.statusMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (message.contains("Successfully")) finish()
        }


        currentIsbn = intent.getStringExtra("SCAN_RESULT_ISBN")
        if (!currentIsbn.isNullOrEmpty()) {
            binding.editBookIsbn.setText(currentIsbn)
            val currentUserId = UserSession.currentUser?.id ?: -1
            viewModel.scanAndSaveBook(currentIsbn!!, currentUserId)
        }

        val existingBookId = intent.getIntExtra("BOOK_ID", -1)
        if (existingBookId != -1) {
            this.bookId = existingBookId
            binding.btnDeleteBook.visibility = android.view.View.VISIBLE

            viewModel.getBooksForUser(UserSession.currentUser?.id ?: -1).observe(this) { books ->
                val book = books.find { it.id == existingBookId }
                book?.let {
                    populateFields(it)

                    binding.btnDeleteBook.setOnClickListener {
                        showDeleteConfirmation(book)
                    }
                }
            }

        }
        binding.btnSaveBook.setOnClickListener { saveBook() }
        binding.btnBackBook.setOnClickListener { finish() }
        binding.imgBookCoverPreview.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
    }
    private fun showDeleteConfirmation(book: Book) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to remove '${book.title}' from your library?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteBook(book)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun populateFields(book: Book) {
        binding.editBookTitle.setText(book.title)
        binding.editBookAuthor.setText(book.author)
        binding.editBookIsbn.setText(book.isbn)
        binding.editBookSummary.setText(book.summary)
        binding.editBookPages.setText(book.pageCount?.toString() ?: "")
        this.existingCoverPath = book.coverImagePath ?: book.imageUrl
        this.selectedBookcaseId = book.bookcaseId

        val currentShelf = allBookcases.find { it.id == book.bookcaseId }
        currentShelf?.let {
            binding.autoCompleteBookcase.setText(it.shelfName, false)
        }

        if (!this.existingCoverPath.isNullOrEmpty()) {
            binding.imgBookCoverPreview.load(this.existingCoverPath)
        }
    }

    private fun saveBook() {
        val title = binding.editBookTitle.text.toString().trim()
        val author = binding.editBookAuthor.text.toString().trim()
        val currentUserId = UserSession.currentUser?.id ?: return

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields (*)", Toast.LENGTH_SHORT).show()
            return
        }

        val book = Book(
            id = if (bookId > 0) bookId else 0,
            ownerId = currentUserId,
            title = title,
            author = author,
            isbn = binding.editBookIsbn.text.toString().trim(),
            publisher = binding.editBookPublisher.text.toString().trim(),
            publishedYear = binding.editBookYear.text.toString().toIntOrNull(),
            tagsGenre = binding.editBookGenre.text.toString().trim(),
            pageCount = binding.editBookPages.text.toString().toIntOrNull() ?: 0,
            summary = binding.editBookSummary.text.toString().trim(),
            myNotes = binding.editBookNotes.text.toString(),
            status = "Owned",
            isLoaned = binding.switchIsLoaned.isChecked,
            bookcaseId = selectedBookcaseId,
            coverImagePath = selectedImageUri?.toString() ?: existingCoverPath
        )
        viewModel.addBook(book)
        Toast.makeText(this, "Successfully saved $title", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun SetupBookcaseSpinner() {
        val userId = UserSession.currentUser?.id ?: -1

        viewModel.getAllBookcases(userId).observe(this) { bookcases ->
            allBookcases = bookcases

            val nameList = bookcases.map { it.shelfName }

            val adapter = ArrayAdapter<String>(
                this@EditBookActivity,
                android.R.layout.simple_dropdown_item_1line,
                nameList
            )

            val autoView = binding.autoCompleteBookcase as? AutoCompleteTextView
            autoView?.setAdapter(adapter)

            autoView?.setOnItemClickListener { _, _, position, _ ->
                val selectedShelf = allBookcases[position]
                selectedBookcaseId = selectedShelf.id
            }

            if (bookId != -1) {
                val currentBooks = viewModel.getBooksForUser(userId).value
                val currentBook = currentBooks?.find { it.id == bookId }
                val currentShelf = allBookcases.find { it.id == currentBook?.bookcaseId }

                currentShelf?.let { shelf ->
                    autoView?.setText(shelf.shelfName, false)
                    selectedBookcaseId = shelf.id
                }
            }
        }
    }
}