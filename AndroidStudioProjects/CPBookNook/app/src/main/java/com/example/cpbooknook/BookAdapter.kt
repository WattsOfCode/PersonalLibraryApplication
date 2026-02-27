package com.example.cpbooknook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cpbooknook.databinding.ItemBookBinding
import coil.load
/** **********************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * Connects the list of book objects to the visual list
 * items (item_book.xml) displayed in the RecyclerView within
 * LibraryActivity. It is responsible for inflating the view
 * layout and binding the book data to it.
 * ********************************************************** */
class BookAdapter(
    private var books: List<Book>,
    private val onItemClicked: (Book) -> Unit // Click handler lambda
    ) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // Inner class that holds the View for each single item in the list
    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            // Set data from the Book object to the views in the xml layouts
            binding.imgBookCover.load(book.imageUrl) {
                placeholder(R.drawable.ic_book_placeholder)
                error(R.drawable.ic_book_placeholder)
                crossfade(true)
            }

            binding.textBookTitle.text = book.title
            binding.textBookAuthor.text = itemView.context.getString(R.string.db_string_author, book.author)
            binding.textBookGenre.text = book.tagsGenre
            binding.textBookPages.text = itemView.context.getString(R.string.db_int_pages, book.pageCount)
            val isbnText = if (book.isbn.isNullOrBlank()) {
                "ISBN: N/A"
            } else {
                "ISBN: ${book.isbn}"
            }
            binding.textBookIsbn.text = isbnText

            // Set the click listener on the entire CardView
            binding.root.setOnClickListener {
                onItemClicked(book)
            }
        }
    }

    // 1. Creates the ViewHolder (Inflates the item_book.xml layout)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    // 2. Binds the data to the ViewHolder
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    // 3. Returns the total number of items
    override fun getItemCount(): Int = books.size

    /**
     * Updates the data set and notifies the RecyclerView to refresh the list.
     * Called from LibraryActivity after loading or searching.
     */
    fun updateList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}