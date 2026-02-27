
package com.example.booknookv2
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {
    @GET("books/v1/volumes")
    suspend fun searchByIsbn(
        @Query("q") isbnQuery: String
    ): GoogleBooksResponse
}
data class GoogleBooksResponse(val items: List<BookItem>?)
data class BookItem(val volumeInfo: VolumeInfo)
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val imageLinks: ImageLinks?
)
data class ImageLinks(val thumbnail: String?)