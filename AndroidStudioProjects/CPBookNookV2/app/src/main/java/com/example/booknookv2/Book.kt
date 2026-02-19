package com.example.booknookv2

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Bookcase::class,
            parentColumns = ["id"],
            childColumns = ["bookcaseId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerId: Int,
    val bookcaseId: Int?,
    val title: String,
    val author: String,
    val publisher: String? = null,
    val publishedYear: Int? = null,
    val pageCount: Int? = null,
    val tagsGenre: String? = null,
    val isbn: String? = null,
    val summary: String? = null,
    val isLoaned: Boolean = false,
    val status: String? = "Owned",

    val coverImagePath: String? = null,
    val imageUrl: String? = null,

    val myNotes: String? = null
)