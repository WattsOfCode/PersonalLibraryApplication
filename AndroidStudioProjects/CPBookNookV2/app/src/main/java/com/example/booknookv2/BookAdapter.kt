package com.example.booknookv2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.booknookv2.databinding.ItemBookBinding
import coil.load

class BookAdapter(
    private var books: List<Book>,
    private val onItemClicked: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {

            binding.textBookTitle.text = book.title
            binding.textBookAuthor.text = book.author ?: "Unknown Author"
            binding.textBookGenre.text = book.tagsGenre

            // Handle page count safely
            binding.textBookPages.text = if (book.pageCount != null && book.pageCount > 0) {
                "• ${book.pageCount} pages"
            } else {
                ""
            }

            // Coil handles the image source (Local Path or Web URL) automatically!
            val imageSource = book.coverImagePath ?: book.imageUrl
            binding.imgBookCover.load(imageSource) {
                placeholder(R.drawable.ic_book_placeholder)
                error(R.drawable.ic_book_placeholder)
                crossfade(true)
            }

            // Loan Status Badge
            binding.textLoanStatus.isVisible = book.isLoaned
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
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}