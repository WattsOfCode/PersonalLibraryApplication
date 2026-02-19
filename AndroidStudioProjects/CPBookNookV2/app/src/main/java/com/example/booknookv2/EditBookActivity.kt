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

    private lateinit var binding: ActivityEditBookBinding
    private lateinit var viewModel: BookViewModel
    private var selectedImageUri: Uri? = null

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

        val scannedIsbn = intent.getStringExtra("SCAN_RESULT_ISBN")
        if (!scannedIsbn.isNullOrEmpty()) {
            binding.editBookIsbn.setText(scannedIsbn)
            fetchBookInfo(scannedIsbn)
        }

        val currentUserId = UserSession.currentUser?.id ?: -1

        viewModel.getAllBookcases(currentUserId).observe(this) { shelves: List<Bookcase> ->
            allBookcases = shelves
            val shelfNames = shelves.map { it.shelfName }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shelfNames)
            binding.autoCompleteBookcase.setAdapter(adapter)
        }

        binding.autoCompleteBookcase.setOnItemClickListener { _, _, position, _ ->
            selectedBookcaseId = allBookcases[position].id
        }

        binding.imgBookCoverPreview.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSaveBook.setOnClickListener { saveBook() }
        binding.btnBackBook.setOnClickListener { finish() }
    }

    private fun saveBook() {
        val title = binding.editBookTitle.text.toString().trim()
        val author = binding.editBookAuthor.text.toString().trim()
        val isbn = binding.editBookIsbn.text.toString().trim()
        val genre = binding.editBookGenre.text.toString().trim()
        val pages = binding.editBookPages.text.toString().toIntOrNull() ?: 0
        val summary = binding.editBookSummary.text.toString().trim()
        val isLoaned = binding.switchIsLoaned.isChecked

        val status = when (binding.radioGroupStatus.checkedRadioButtonId) {
            R.id.radio_status_wishlist -> "Wishlist"
            R.id.radio_status_read -> "Read"
            else -> "Owned"
        }

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields (*)", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUserId = UserSession.currentUser?.id ?: return

        val book = Book(
            ownerId = currentUserId,
            title = title,
            author = author,
            isbn = isbn,
            tagsGenre = genre,
            pageCount = pages,
            summary = summary,
            myNotes = binding.editBookNotes.text.toString(),
            status = status,
            isLoaned = isLoaned,
            bookcaseId = selectedBookcaseId,
            coverImagePath = selectedImageUri?.toString()
        )

        viewModel.addBook(book)
        Toast.makeText(this, "Successfully saved $title", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun fetchBookInfo(isbn: String) {
        val url = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn"

        Thread {
            try {
                val response = java.net.URL(url).readText()
                val jsonObject = org.json.JSONObject(response)
                val items = jsonObject.optJSONArray("items")

                if (items != null && items.length() > 0) {
                    val info = items.getJSONObject(0).getJSONObject("volumeInfo")
                    val title = info.optString("title")
                    val authorsArray = info.optJSONArray("authors")
                    val authors = mutableListOf<String>()

                    if (authorsArray != null) {
                        for (i in 0 until authorsArray.length()) {
                            authors.add(authorsArray.getString(i))
                        }
                    }

                    val finalAuthorsString = authors.joinToString(", ")
                    val description = info.optString("description")
                    val pageCount = info.optInt("pageCount")
                    val imageLinks = info.optJSONObject("imageLinks")
                    val thumbnail = imageLinks?.optString("thumbnail")?.replace("http:", "https:")

                    runOnUiThread {
                        if (!isFinishing) {
                            binding.editBookTitle.setText(title)
                            binding.editBookAuthor.setText(if (finalAuthorsString.isNotEmpty()) finalAuthorsString else "Unknown Author")
                            binding.editBookSummary.setText(description)
                            binding.editBookPages.setText(if (pageCount > 0) pageCount.toString() else "")

                            if (!thumbnail.isNullOrEmpty()) {
                                binding.imgBookCoverPreview.load(thumbnail)
                                selectedImageUri = android.net.Uri.parse(thumbnail)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Failed to find book details", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}