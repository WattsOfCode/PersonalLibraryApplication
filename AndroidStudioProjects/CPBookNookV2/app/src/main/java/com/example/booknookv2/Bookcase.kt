package com.example.booknookv2

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookcases",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Bookcase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerId: Int,
    val shelfName: String,
    val shelfDescription: String? = null
)