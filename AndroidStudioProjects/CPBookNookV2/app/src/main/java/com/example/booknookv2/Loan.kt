package com.example.booknookv2

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey


@Entity(
    tableName = "Loans", // Added missing comma here
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Loan(
    @PrimaryKey(autoGenerate = true)
    val loanId: Int = 0,
    val bookId: Int,
    val borrowerName: String,
    val loanDate: Long,
    val dueDate: Long?,
    val isReturned: Boolean = false
)