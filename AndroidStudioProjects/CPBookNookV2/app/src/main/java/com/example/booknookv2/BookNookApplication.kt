package com.example.booknookv2

import android.app.Application

class BookNookApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        BookRepository(database.bookDao(), database.bookcaseDao())
    }
}