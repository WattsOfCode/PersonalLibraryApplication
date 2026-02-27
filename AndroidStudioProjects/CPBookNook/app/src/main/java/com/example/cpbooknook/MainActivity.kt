package com.example.cpbooknook
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cpbooknook.databinding.ActivityMainBinding
/**  *****************************************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * The starting screen of the application.
 *   *****************************************************************************/
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: BookController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Controller
        val application = application as BookNookApplication
        controller = application.bookController

        // Load and display initial data (stats)
        updateStats()

        // Set up navigation/actions
        setupActions()
    }
    override fun onResume() {
        super.onResume()
        // Refresh data every time the user returns to the MainActivity
        updateStats()
    }

    // Retrieves book data from the Controller and updates the UI elements in activity_main.xml.
    private fun updateStats() {
        // The Activity asks the Controller for the data list
        val books = controller.getLibraryBooks()

        // Calculate stats
        val totalCount = books.size
        val wishlistCount = books.count { it.status == "Wishlist" }

        //update text views
        binding.dbCountBooks.text = "Total Books: $totalCount"

        binding.dbWishlistCount.text = "Books on wish list: $wishlistCount"
    }


    // Sets up listeners for buttons that navigate or trigger actions.

    private fun setupActions() {
        // Example action: Open the Library View
        binding.btnMyLibrary.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }

        // Example action: Open the Add Book View
        binding.btnAddBook.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }
}