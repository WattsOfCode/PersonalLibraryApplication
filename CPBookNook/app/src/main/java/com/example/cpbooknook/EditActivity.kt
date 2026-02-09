package com.example.cpbooknook
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cpbooknook.databinding.EditActivityBinding
import coil.load
/** *************************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * The screen responsible for handling a single book's details.
 * It contains the logic for both Adding a new book, Editing an
 * existing book (loading its data), and Deleting a book,
 * including logic for image selection.
 * ************************************************************** */
class EditActivity : AppCompatActivity() {
    private lateinit var binding: EditActivityBinding
    private lateinit var controller: BookController
    private var bookIdToEdit: Int = 0
    private var currentImageUri: String? = null
    private val selectImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    currentImageUri = it.toString()
                    binding.imgBookCoverPreview.load(it)
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Initialize Controller
        val application = application as BookNookApplication
        controller = application.bookController

        // 3. Check for incoming Book ID
        // Intent uses Long by default for large IDs, but we use Int for ISBN. Keep ID as Long.
        bookIdToEdit = intent.getIntExtra("BOOK_ID", 0)

        // 4. Load Existing Data or Prepare for New Entry
        if (bookIdToEdit != 0) {

            //update the controller to have a get bookById
            loadBookData(bookIdToEdit)
            binding.textAddTitle.text = getString(R.string.txt_title_edit)
            binding.btnDeleteBook.visibility = android.view.View.VISIBLE
        } else {
            binding.textAddTitle.text = getString(R.string.txt_title_add)
            binding.btnDeleteBook.visibility = android.view.View.GONE
        }
        setupActions()
    }

    private fun loadBookData(id: Int) {
        // We will need to implement controller.getBookById() soon
        val book = controller.getBookById(id)

        if (book != null) {
            // Fill the fields with the existing book's data
            binding.editBookTitle.setText(book.title)
            binding.editBookAuthor.setText(book.author)
            binding.editBookIsbn.setText(book.isbn.toString())
            binding.editBookPages.setText(book.pageCount.toString())
            binding.editBookGenre.setText(book.tagsGenre)
            binding.editBookPublisher.setText(book.publisher)
            binding.editBookSummary.setText(book.summary)
            binding.editBookNotes.setText(book.myNotes)

            book.imageUrl?.let { uriString ->
                currentImageUri = uriString
                binding.imgBookCoverPreview.load(Uri.parse(uriString)) {
                    placeholder(R.drawable.ic_book_placeholder)
                    error(R.drawable.ic_book_placeholder)
                }
            }

            // Set the correct RadioButton based on book.status
            when (book.status) {
                "Read" -> binding.radioStatusRead.isChecked = true
                "Wishlist" -> binding.radioStatusWishlist.isChecked = true
                else -> binding.radioStatusOwned.isChecked = true
            }
        } else {
            Toast.makeText(this, "Error: Book not found.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupActions() {
        // Back Button
        binding.btnBackBook.setOnClickListener { finish() }
        // Save Button
        binding.btnSaveBook.setOnClickListener { saveBook() }
        binding.imgBookCoverPreview.setOnClickListener { openImageChooser() }
        binding.btnDeleteBook.setOnClickListener { showDeleteConfirmationDialog() }
    }

    //create launches and intent to pick image
    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        selectImageLauncher.launch(intent)
    }

    private fun saveBook() {
        // Input Validation
        val title = binding.editBookTitle.text.toString().trim()
        val author = binding.editBookAuthor.text.toString().trim()

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Title and Author are required.", Toast.LENGTH_SHORT).show()
            return
        }

        // Collect all data and safely convert to numbers (using null-safe Elvis operator)
        val newIsbn = binding.editBookIsbn.text.toString().trim()
        val newPages = binding.editBookPages.text.toString().toIntOrNull()

        // Get status from radio group
        val newStatus = when (binding.radioGroupStatus.checkedRadioButtonId) {
            binding.radioStatusRead.id -> "Read"
            binding.radioStatusWishlist.id -> "Wishlist"
            else -> "Owned" // Default status
        }

        // Create the final Book object
        val finalBook = Book(
            // Use existing ID if editing, otherwise use current timestamp for a new unique ID
            id = bookIdToEdit,
            title = title,
            author = author,
            isbn = newIsbn.ifEmpty { null },
            pageCount = newPages,
            publisher = binding.editBookPublisher.text.toString().trim(),
            summary = binding.editBookSummary.text.toString().trim(),
            myNotes = binding.editBookNotes.text.toString().trim(),
            status = newStatus,
            tagsGenre = binding.editBookGenre.text.toString().trim().ifEmpty { null },
            yearPublished = Int,
            imageUrl = currentImageUri
        )

        // Save/Update via Controller
        if (bookIdToEdit != 0) {
            controller.updateBook(finalBook) // Controller updates the existing entry
            Toast.makeText(this, "${finalBook.title} updated!", Toast.LENGTH_SHORT).show()
        } else {
            controller.addBook(finalBook) // Controller adds a new entry
            Toast.makeText(this, "${finalBook.title} added!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    //confirmation dialog before deleting the book
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.txt_delete_book).trim())
            .setMessage("Are you sure you want to permanently delete this book from your library?")
            .setPositiveButton("Delete") { dialog, _ ->
                // User confirmed deletion
                deleteBook()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
    //deletes the book from the library and closes the activity
    private fun deleteBook() {
        val bookToDelete = controller.getBookById(bookIdToEdit)
        if (bookToDelete != null) {
            //delete the book using the controller
            controller.deleteBook(bookToDelete)
            Toast.makeText(this, "${bookToDelete.title} deleted", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(this, "Error: Could not find book to delete.", Toast.LENGTH_SHORT).show()
        }
    }
}