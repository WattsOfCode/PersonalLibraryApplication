package com.example.booknookv2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.booknookv2.databinding.ActivityDashboardBinding
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]
        val currentUserId = UserSession.currentUser?.id ?: -1

        setupStats(currentUserId)
        binding.btnAddBook.setOnClickListener {
            startActivity(Intent(this, EditBookActivity::class.java))
        }
        binding.btnAddShelf.setOnClickListener {
            showAddBookcaseDialog()
        }
        binding.btnScanBarcode.setOnClickListener {
            val scanner = GmsBarcodeScanning.getClient(this)

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val isbn = barcode.rawValue ?: return@addOnSuccessListener
                    val currentUserId = UserSession.currentUser?.id ?: -1

                    viewModel.scanAndSaveBook(isbn, currentUserId)
                    showScanSuccessDialog(isbn)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Scan failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnMyLibrary.setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }

        binding.btnAddBook.setOnClickListener {
            startActivity(Intent(this, EditBookActivity::class.java))
        }
    }
    private fun showScanSuccessDialog(isbn: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Book Scanned")
            .setMessage("ISBN $isbn is being processed. Would you like to view/edit the details or stay here to scan another?")
            .setPositiveButton("Scan Another") { dialog, _ ->
                dialog.dismiss() // Just closes the popup, stays on Dashboard
            }
            .setNeutralButton("View/Edit") { _, _ ->
                // Only goes to EditPage if they explicitly click this
                val intent = Intent(this, EditBookActivity::class.java)
                intent.putExtra("SCAN_RESULT_ISBN", isbn)
                startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }
    private fun setupStats(userId: Int) {
        viewModel.getBooksForUser(userId).observe(this) { books ->
            binding.dbCountBooks.text = "Books: ${books.size}"
            binding.dbWishlistCount.text = "Wishlist: ${books.count { it.status == "Wishlist" }}"
        }
    }
    private fun showAddBookcaseDialog() {
        val input = android.widget.EditText(this)
        input.hint = "Shelf Name"

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("New Bookcase")
            .setView(input)
            .setPositiveButton("Create") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    val userId = UserSession.currentUser?.id ?: -1
                    val newBookcase = Bookcase(ownerId = userId, shelfName = name)
                    viewModel.addBookcase(newBookcase)
                    Toast.makeText(this, "'$name' added to your library!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}