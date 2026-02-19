package com.example.booknookv2

import androidx.room.Embedded
import androidx.room.Relation

data class LoanWithBook(
    @Embedded val loan: Loan,
    @Relation(
        parentColumn = "bookId", // The column in the Loan entity
        entityColumn = "id"      // The column in the Book entity
    )
    val book: Book
)