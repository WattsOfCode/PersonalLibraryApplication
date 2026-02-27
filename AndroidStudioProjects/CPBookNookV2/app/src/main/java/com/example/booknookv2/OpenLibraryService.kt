package com.example.booknookv2

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryService {
    @GET("api/books?format=json&jscmd=details")
    suspend fun getBookByIsbn(
        @Query("bibkeys") isbnKey: String
    ): Map<String, OpenLibraryBook>
}

data class OpenLibraryBook(
    val details: OpenLibraryDetails?,
    val thumbnail_url: String?
)

data class OpenLibraryDetails(
    val title: String,
    val publishers: List<String>?,
    val number_of_pages: Int?,
    val description: Any?,
    val authors: List<OLAuthor>?
)
data class OLAuthor(val name: String)