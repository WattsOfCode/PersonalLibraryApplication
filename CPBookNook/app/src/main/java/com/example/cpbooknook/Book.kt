package com.example.cpbooknook
/** ************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * Defines the structure for a single book entry.
 * It's now a simple serializable data class.
 ************************************************** */
data class Book(
    val id: Int,
    val title: String? = null,
    val author: String? = null,
    val isbn: String?,
    val publisher: String? = null,
    val yearPublished: Int.Companion,
    val pageCount: Int?,
    val tagsGenre: String? = null,
    val summary: String? = null,
    val myNotes: String? = null,

    val status: String, // e.g., "Owned", "Wishlist", "Read"
    val coverImagePath: String? = null,
    val imageUrl: String? = null
)