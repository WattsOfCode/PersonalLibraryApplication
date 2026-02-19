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

        binding.btnScanBarcode.setOnClickListener {
            val scanner = GmsBarcodeScanning.getClient(this)

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val isbn = barcode.rawValue // The scanned ISBN string

                    val intent = Intent(this, EditBookActivity::class.java)
                    intent.putExtra("SCAN_RESULT_ISBN", isbn)
                    startActivity(intent)
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

    private fun setupStats(userId: Int) {
        viewModel.getBooksForUser(userId).observe(this) { books ->
            binding.dbCountBooks.text = "Books: ${books.size}"
            binding.dbWishlistCount.text = "Wishlist: ${books.count { it.status == "Wishlist" }}"
        }
    }
}