package com.example.booknookv2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.booknookv2.databinding.ItemBookBinding
import coil.load

class BookAdapter(
    private var books: List<Book>,
    private val onItemClicked: (Book) -> Unit // This name must be used below
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            // Use the names that exist in your XML (textBookTitle, etc.)
            binding.textBookTitle.text = book.title
            binding.textBookAuthor.text = book.author ?: "Unknown Author"
            binding.textBookGenre.text = book.tagsGenre

            binding.textBookPages.text = if (book.pageCount != null && book.pageCount > 0) {
                "• ${book.pageCount} pages"
            } else {
                ""
            }

            val imageSource = book.coverImagePath ?: book.imageUrl
            binding.imgBookCover.load(imageSource) {
                placeholder(R.drawable.ic_book_placeholder)
                error(R.drawable.ic_book_placeholder)
                crossfade(true)
            }

            binding.textLoanStatus.isVisible = book.isLoaned

            // This connects the click to the listener passed in the constructor
            binding.root.setOnClickListener {
                onItemClicked(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        // Just call the bind function we wrote above!
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}